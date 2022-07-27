package com.example.cialo.services.eventsscheduler

import android.app.Service
import android.content.Intent
import android.os.IBinder

class EventsSenderService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)

        return START_NOT_STICKY;
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null;
    }
}