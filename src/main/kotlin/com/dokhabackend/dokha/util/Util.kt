package com.dokhabackend.dokha.util

import java.util.*


/**
 * Semenov A.E.
 * Created 22.06.2019
 *
 **/

class Util {

    fun truncDate(date: Long): Long {

        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date(date)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }
}