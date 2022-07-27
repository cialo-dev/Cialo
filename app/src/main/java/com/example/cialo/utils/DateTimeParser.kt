package com.example.cialo.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class DateTimeParser {
    companion object {
        private val sdf: DateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")

        private fun getDateTimeFormat(): DateFormat {
            sdf.timeZone = TimeZone.getTimeZone("UTC")

            return sdf;
        }

        fun toApiString(ticks: Long): String {
            val sdf = getDateTimeFormat();
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = ticks;
            val tz = TimeZone.getDefault()
            sdf.timeZone = tz
            return sdf.format(calendar.time)
        }
    }
}