package com.example.cialo.services.eventsscheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import com.example.cialo.BuildConfig
import com.example.cialo.services.notifications.INotificationService
import com.example.cialo.utils.Operation
import com.example.cialo.utils.RequestCodes

class EventsSenderRunner(
    private val context: Context,
    private val notificationService: INotificationService,
) : IEventSenderRunner {

    private lateinit var _alarmManager: AlarmManager;
    private lateinit var _eventSchedulerIntent: PendingIntent;


    override fun run(): Operation {
        _alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val eventServiceIntent = Intent(context, EventsSenderService::class.java)

        _eventSchedulerIntent = PendingIntent.getService(context,
            RequestCodes.schedulerIntentRequestCode,
            eventServiceIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        _alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime(),
            BuildConfig.SendEventsIntervalMinutes.toLong() * 60000,
            _eventSchedulerIntent)

        return Operation.createSuccess()
    }
}