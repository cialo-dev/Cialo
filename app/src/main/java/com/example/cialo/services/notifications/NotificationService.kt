package com.example.cialo.services.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.cialo.R

class NotificationService : INotificationService {

    private val _channelId = "912";

    override fun display(context: Context, text: String) {
        this.createNotificationChannelIfNotExist(context)

        val notification = NotificationCompat.Builder(context, _channelId)
            .setContentTitle(text)
            .setSmallIcon(R.drawable.ic_home_black_24dp)
            .setContentText("Much longer text that cannot fit one line...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(context)) {
            notify((0..1000).random(), notification.build())
        }
    }

    override fun createProximityNotification(context: Context) : Notification {
        this.createNotificationChannelIfNotExist(context)

        return NotificationCompat.Builder(context, _channelId)
            .setContentTitle("Witaj w klubie")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentText("Jesteś chuju namierzany.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()
    }

    private fun createNotificationChannelIfNotExist(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val descriptionText = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(_channelId, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}