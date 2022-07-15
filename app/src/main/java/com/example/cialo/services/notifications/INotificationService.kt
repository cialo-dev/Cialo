package com.example.cialo.services.notifications

import android.app.Notification
import android.content.Context

public interface INotificationService {
    fun display(context: Context, text: String)

    fun createProximityNotification(context: Context): Notification
}