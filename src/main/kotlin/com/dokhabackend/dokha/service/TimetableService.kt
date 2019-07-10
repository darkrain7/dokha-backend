package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.dto.TimetableDto
import com.dokhabackend.dokha.entity.Timetable
import java.time.DayOfWeek


/**
 * Semenov A.E.
 * Created 18.06.2019
 *
 **/
interface TimetableService {

    fun findByStoreIdAndWorkingDate(storeId: Long, workingDate: Long): Timetable

    fun findAfterCurrentDate(): Collection<Timetable>

    fun findMaxWorkingDateByStoreId(storeId: Long): Timetable?

    fun create(timetableDto: TimetableDto): Timetable

    fun create(timetable: Timetable): Timetable

    fun generateDefaultTimetable(day: Long, storeId: Long): Timetable
}