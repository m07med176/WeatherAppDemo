package com.android.weatherapp.ui.alert

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.ContentResolver
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.android.weatherapp.MainActivity
import com.android.weatherapp.R

private const val CHANNEL_ID = 1
private const val FOREGROUND_ID = 2

// TODO 2.3 Register Service and Implement AlertWindowManager and Notification there
class AlertService : Service() {


    private var notificationManager: NotificationManager? = null
    var alertWindowManger: AlertWindowManger? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        // Get Data from Work Manager
        val description = intent?.getStringExtra("description")

        // Create Notification Channel
        notificationChannel()
        startForeground(FOREGROUND_ID, makeNotification(description!!))

        //start window manger
        if (Settings.canDrawOverlays(this)) {
            alertWindowManger = AlertWindowManger(this, description)
            alertWindowManger!!.initializeWindowManager()
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    private fun makeNotification(description: String): Notification {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        return NotificationCompat.Builder(applicationContext, "$CHANNEL_ID")
            .setSmallIcon(R.drawable.baseline_add_alert_24)
            .setContentText(description)
            .setContentTitle("Weather Alarm")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setAutoCancel(true)
            .build()
    }

    private fun notificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: String = getString(R.string.channel_name)
            val description = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("$CHANNEL_ID",name, importance)
            channel.enableVibration(true)
            channel.description = description
            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }
}