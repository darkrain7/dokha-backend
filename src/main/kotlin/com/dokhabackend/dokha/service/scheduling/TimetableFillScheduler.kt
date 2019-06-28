package com.dokhabackend.dokha.service.scheduling

import com.dokhabackend.dokha.service.TimetableService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by SemenovAE on 28.06.2019

 */


@Component

class TimetableFillScheduler
@Autowired constructor(val timetableService: TimetableService) {

    fun fillTimetable() {

        val nextSevenDays = Calendar.getInstance()
        nextSevenDays.set(Calendar.HOUR_OF_DAY, 0)
        nextSevenDays.set(Calendar.MINUTE, 0)
        nextSevenDays.set(Calendar.SECOND, 0)
        nextSevenDays.add(Calendar.DAY_OF_MONTH, 7)

        val step = Calendar.getInstance()
        step.set(Calendar.DAY_OF_MONTH, 1)

        val map = timetableService.findAfterCurrentDate()
                .groupBy { it.store }
                .entries
                .map { it.value.maxBy { timetable -> timetable.workingDate } }


    }
}