package com.example.cialo.services.logging

import android.util.Log
import com.example.cialo.exceptionHandling.AppError

class DebugLogger : ILog {
    override fun info(name: String, props: Map<String, String>?) {
        Log.i("DebugLogger", name)
    }

    override fun error(ex: Throwable, props: Map<String, String>?) {
        Log.e("DebugLogger","Exception", ex)
    }

    override fun error(error: AppError, props: Map<String, String>?) {
        Log.e("AppError", error.message)
    }

}