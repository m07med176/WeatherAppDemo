package com.android.weatherapp.ui.alert

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.android.weatherapp.R
import com.android.weatherapp.data.local.RoomDB
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

// TODO 1#12 Implement logic of [AlertDialog]
class AlertDialog : DialogFragment() {

    private lateinit var view: View

    // Widgets
    private lateinit var fromBtn: Button
    private lateinit var toBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var cancelBtn: Button

    // AlertModel
    private lateinit var alertModel: AlertModel

    // Initialize ViewModel
    private val viewModel: AlertViewModel by lazy {
        val dao = RoomDB.invoke(requireContext()).alertDao()
        ViewModelProvider(
            requireActivity(),
            AlertViewModelFactory(dao)
        )[AlertViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        view = inflater.inflate(R.layout.fragment_alert_dialog, container, false)

        // Catch Widgets
        fromBtn = view.findViewById(R.id.btn_date_from)
        toBtn = view.findViewById(R.id.btn_date_to)
        saveBtn = view.findViewById(R.id.btn_save)
        cancelBtn = view.findViewById(R.id.btn_cancel)

        // initial values of date and time
        setInitialData()

        // Events
        fromBtn.setOnClickListener {
            showDatePicker(true)
        }

        toBtn.setOnClickListener {
            showDatePicker(false)
        }

        saveBtn.setOnClickListener {
            viewModel.insertAlert(alertModel)
            dialog!!.dismiss()
        }

        cancelBtn.setOnClickListener {
            dialog!!.dismiss()
        }

        // observe Insert
        lifecycleScope.launch {
            viewModel.stateInsetAlert.collect{id->
                // Register Worker Here and send ID of alert
            }
        }
        return view
    }

    override fun onStart() {
        super.onStart()
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }


    private fun setInitialData() {
        val rightNow = Calendar.getInstance()
        // init time
        val currentHour = TimeUnit.HOURS.toSeconds(rightNow.get(Calendar.HOUR_OF_DAY).toLong())
        val currentMinute = TimeUnit.MINUTES.toSeconds(rightNow.get(Calendar.MINUTE).toLong())
        val currentTime = (currentHour + currentMinute).minus(3600L * 2)
        val currentTimeText = timeConverterToString((currentTime + 60), requireContext())
        val afterOneHour = currentTime.plus(3600L)
        val afterOneHourText = timeConverterToString(afterOneHour, requireContext())
        // init day
        val year = rightNow.get(Calendar.YEAR)
        val month = rightNow.get(Calendar.MONTH)
        val day = rightNow.get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = convertDateToLong(date, requireContext())
        val currentDate = dayConverterToString(dayNow, requireContext())
        //init model
        alertModel =
            AlertModel(
                startTime = (currentTime + 60),
                endTime = afterOneHour,
                startDate = dayNow,
                endDate = dayNow
            )
        //init text
        fromBtn.text = currentDate.plus("\n").plus(currentTimeText)
        toBtn.text = currentDate.plus("\n").plus(afterOneHourText)
    }

    private fun showTimePicker(isFrom: Boolean, datePicker: Long) {
        val rightNow = Calendar.getInstance()
        val currentHour = rightNow.get(Calendar.HOUR_OF_DAY)
        val currentMinute = rightNow.get(Calendar.MINUTE)
        val listener: (TimePicker?, Int, Int) -> Unit =
            { _: TimePicker?, hour: Int, minute: Int ->
                val time = TimeUnit.MINUTES.toSeconds(minute.toLong()) +
                        TimeUnit.HOURS.toSeconds(hour.toLong()) - (3600L * 2)
                val dateString = dayConverterToString(datePicker, requireContext())
                val timeString = timeConverterToString(time, requireContext())
                val text = dateString.plus("\n").plus(timeString)
                if (isFrom) {
                    alertModel.startTime = time
                    alertModel.startDate = datePicker
                    fromBtn.text = text
                } else {
                    alertModel.endTime = time
                    alertModel.endDate = datePicker
                    toBtn.text = text
                }
            }

        val timePickerDialog = TimePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            listener, currentHour, currentMinute, false
        )

        timePickerDialog.setTitle("Choose time")
        timePickerDialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        timePickerDialog.show()
    }

    private fun showDatePicker(isFrom: Boolean) {
        val myCalender = Calendar.getInstance()
        val year = myCalender[Calendar.YEAR]
        val month = myCalender[Calendar.MONTH]
        val day = myCalender[Calendar.DAY_OF_MONTH]
        val myDateListener =
            DatePickerDialog.OnDateSetListener { view, year, month, day ->
                if (view.isShown) {
                    val date = "$day/${month + 1}/$year"
                    showTimePicker(isFrom, convertDateToLong(date, requireContext()))
                }
            }
        val datePickerDialog = DatePickerDialog(
            requireContext(), android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
            myDateListener, year, month, day
        )
        datePickerDialog.setTitle("Choose date")
        datePickerDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        datePickerDialog.show()
    }


}