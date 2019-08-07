package com.dokhabackend.dokha.dto

import java.time.LocalDate
import java.time.LocalTime

/**
 * Created by SemenovAE on 28.06.2019

 */

data class TimetableDto(

        val id: Long?,

        val startTime: LocalTime,

        val endTime: LocalTime,

        val workingDate: LocalDate,

        val workingDay: Boolean,

        val storeId: Long
)