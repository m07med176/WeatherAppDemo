package com.android.weatherapp.ui.alert

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.R
import com.android.weatherapp.data.local.RoomDB
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * # Alert System
 * ### 1. Display Alerts & Alert Dialog [AlertDialog] & Alert Settings
 * ### 2. Worker & Time Estimation
 * ### 3. Service [Notification, Display on Top]
 *
 *  __
 * ## [1] Display Alerts & Alert Dialog
 * 1. Create Entity of Alert  [AlertModel]
 * 2. Create DAO of Alert Entity [AlertDao]
 *      * get all alerts
 *      * delete alert by **ID**
 *      * get alert by **ID**
 *      * insert alert **return ID**
 * 3. Register Entity in room and upgrade level [com.android.weatherapp.data.local.RoomDB]
 * 4. Design XML of alert fragment
 *      * recycler view
 *      * float button
 * 5. Design XML of alert fragment dialog
 *      * date from button
 *      * date to button
 *      * save button
 *      * cancel button
 * 6. Design XML Item
 *      * TextView Date Time From
 *      * TextView Date TIme TO
 *      * Close Button
 *      * `Note:` you can add Location if you want or get it from shared preference
 * 7. Create Adapter of alert
 *      * use timestamp Converter in [AlertUtils] file
 *      * Delete Action
 * 8. Create ViewModel of Alert & ViewModelFactory  [AlertViewModel],[AlertViewModelFactory]
 *      * Put functions of dao
 *      * create state flows for [AlertList,AlertItem,ID]
 * 9. Initialize ViewModel,Handel Recycler and float button event View in AlertFragment
 * 10. Design Settings and Shared Preference
 *      * `Note` i am going to put settings in same file of alert fragment you can put it in different fragment
 * 11. Prepare Permission for Display ON Top and Alarm Notification
 *      * Put Permissions in Manifest
 *      ``` xml
 *          <uses-permission android:name="android.permission.ACTION_MANAGE_OVERLAY_PERMISSION" />
 *          <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
 *          <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
 *           <uses-permission android:name="android.permission.WAKE_LOCK" />
 *           <uses-permission android:name="android.permission.VIBRATE" />
 *      ```
 *     * Create Function to check permission of overlay
 * 12. Implement logic of [AlertDialog]
 *      * Initialize viewModel
 *      * use date picker [Date and Time]
 *      * Collect and Save [AlertModel]
 * --
 * ## [2] Service Notification, Display on Top
 * 1. Design XML of alert display overlay
 * 2. Implement [AlertWindowManger]
 *      * inflate view
 *      * register view in window manager
 *      * function to close view and remove it from window manager
 *      * function to close [AlertService]
 * 3. Register Service and Implement AlertWindowManager and Notification there
 *      * Create Service class
 *      * register it in manifest
 *       ``` xml
 *          <service
 *              android:name=".ui.alert.AlertService"
 *              android:enabled="true"
 *              android:exported="true" />
 *         ```
 *      * Implement Notification and Notification Channel and Display on top if permitted
 * 4. Create Function TO Intent Service
 */


// TODO 1#9 Initialize ViewModel,Handel Recycler and float button event View in AlertFragment
class AlertFragment : Fragment() {

    private lateinit var view:View
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AlertAdapter
    private lateinit var floatingActionButton: FloatingActionButton


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
        view = inflater.inflate(R.layout.fragment_alert, container, false) // You Can use binding or data binding as you want

        // Catch Widgets
        recyclerView = view.findViewById(R.id.recyclerView)
        floatingActionButton = view.findViewById(R.id.floatingActionButton)

        adapter = AlertAdapter(requireContext()){
            // On Delete Icon Clicked
            // Should Delete Item
            viewModel.deleteAlert(it)
        }
        recyclerView.adapter = adapter


        viewModel.getAlerts()

        lifecycleScope.launch{
            viewModel.stateGetAlert.collect{
                adapter.differ.submitList(it)
            }
        }
        // Float Action Event
        floatingActionButton.setOnClickListener {
            // Initialize Here Alert Dialog Fragment
            AlertDialog().show(requireActivity().supportFragmentManager, "AlertDialogFragment")
        }

        // For Settings Purpose
        settingsManager()

        return view
    }

    private fun settingsManager() {
        // TODO 1#10 Design Settings and Shared Preference
        // Settings Widgets
        val notificationRadio:RadioButton = view.findViewById(R.id.notificationRadio)
        val dialogRadio:RadioButton = view.findViewById(R.id.alertRadio)

        // Initialize Shared Preference
        val sharedPreferences = requireContext().getSharedPreferences("MySettings",Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Return State if radio buttons
        val isDialogState = sharedPreferences.getBoolean("IsDialog",false)
        if (isDialogState){
            notificationRadio.isChecked = false
            dialogRadio.isChecked = true
        }else{
            notificationRadio.isChecked = true
            dialogRadio.isChecked = false
        }

        // Events
        notificationRadio.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                editor.putBoolean("IsDialog", false)
                editor.apply()
            }
        }
        dialogRadio.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked) {
                editor.putBoolean("IsDialog", true)
                editor.apply()

                checkPermissionOfOverlay()
            }
        }


        // Start Service Just for test
        lifecycleScope.launch {
            delay(5000)
            startAlertService("this is for test only")
        }

    }


    private fun checkPermissionOfOverlay() {
        // TODO 1#11 Prepare Permission for Display ON Top and Alarm Notification
        // Check if we already  have permission
        if (!Settings.canDrawOverlays(requireContext())) {

            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("Display on top")
                .setMessage("You Should let us to draw on top")
                .setPositiveButton("Okay") { dialog: DialogInterface, _: Int ->

                    // Navigate to Manage Overlay settings in device
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().applicationContext.packageName)
                    )
                    startActivityForResult(intent,1)
                    dialog.dismiss()

                }.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }.show()
        }
    }



    private fun startAlertService(description: String) {
        val intent = Intent(requireContext(), AlertService::class.java)
        intent.putExtra("description", description)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(requireContext(), intent)
        } else {
            requireActivity().startService(intent)
        }
    }
}
