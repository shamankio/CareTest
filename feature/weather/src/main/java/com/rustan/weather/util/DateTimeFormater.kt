package com.rustan.weather.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateTimeFormater {
    fun timeFormatterHHmm(date: Date): String{
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        formatter.timeZone = TimeZone.getDefault()
       return formatter.format(date)
    }
    fun dateFormatteryyyyMMdd(date: Date): String{
       return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date)
    }

}