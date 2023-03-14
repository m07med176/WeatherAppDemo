package com.android.weatherapp.ui.alert

import android.content.Context
import androidx.work.*
import com.android.weatherapp.data.Repository
import kotlinx.coroutines.flow.first
import java.util.*
import java.util.concurrent.TimeUnit

// TODO 3#2 Create Periodic Worker
class AlertPeriodicWorkManger(private val context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {


    val repository = Repository.getInstance(context)

    override suspend fun doWork(): Result {
        if (!isStopped) {
            val id = inputData.getLong("id", -1)
            getData(id.toInt())
        }
        return Result.success()
    }

    private suspend fun getData(id: Int) {
        // request data from room or network
        val currentWeather = repository.getHomeCash().first().weather
        val alert = repository.getAlert(id)

        if (checkTimeLimit(alert)) {
            val delay: Long = getDelay(alert)
            if (currentWeather?.alerts.isNullOrEmpty()) {
                setOneTimeWorkManger(
                    delay,
                    alert.id,
                    currentWeather?.current?.weather?.get(0)?.description ?: "",
                )
            } else {
                setOneTimeWorkManger(
                    delay,
                    alert.id,
                    currentWeather?.alerts?.get(0)?.tags?.get(0) ?:"",
                )
            }
        } else {
            repository.deleteAlert(id)
            WorkManager.getInstance().cancelAllWorkByTag("$id")
        }
    }

    private fun setOneTimeWorkManger(delay: Long, id: Int?, description: String) {
        val data = Data.Builder()
        data.putString("description", description)

        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()


        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            AlertOneTimeWorkManger::class.java,
        )
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )
    }

    private fun getDelay(alert: AlertModel): Long {
        val hour = TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute = TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alert.startTime!! - ((hour + minute) - (2 * 3600L))
    }

    private fun checkTimeLimit(alert: AlertModel): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = convertDateToLong(date,context)

        return dayNow >= alert.startDate!!
                &&
                dayNow <= alert.endDate!!
    }



}