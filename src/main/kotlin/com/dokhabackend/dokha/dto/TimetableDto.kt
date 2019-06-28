package com.dokhabackend.dokha.dto

/**
 * Created by SemenovAE on 28.06.2019

 */

data class TimetableDto(

        val id: Long?,

        val startDate: Long,

        val endDate: Long,

        val workingDate: Long,

        val workingDay: Boolean,

        val storeId: Long
)