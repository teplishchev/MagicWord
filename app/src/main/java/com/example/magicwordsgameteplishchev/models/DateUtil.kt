package com.example.magicwordsgameteplishchev.models

import java.text.SimpleDateFormat
import java.util.*

class DateUtil {
    var calendar = Calendar.getInstance()

    fun getCurrentDate(): String? {
        val dateFormat = SimpleDateFormat("dd:MM:yyyy")
        return dateFormat.format(calendar.time)
    }
}