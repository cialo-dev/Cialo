package com.example.cialo.services.logging

import com.example.cialo.exceptionHandling.AppError
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes

class AppCenterLogger : ILog {

    override fun info(name: String, props: Map<String, String>?) {
        Analytics.trackEvent(name, props)
    }

    override fun error(ex: Throwable, props: Map<String, String>?) {
        Crashes.trackError(ex, props, null)
    }

    override fun error(error: AppError, props: Map<String, String>?) {
        //TODO:
    }
}