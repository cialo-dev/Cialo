package com.example.cialo.services.logging

import com.example.cialo.exceptionHandling.AppError

interface ILog {
    fun info(name: String, props: Map<String, String>? = null);
    fun error(ex: Throwable, props: Map<String, String>? = null);
    fun error(error: AppError, props: Map<String, String>? = null);
}