package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.dto.TimetableDto
import com.dokhabackend.dokha.entity.Timetable


/**
 * Semenov A.E.
 * Created 18.06.2019
 *
 **/
interface TimetableService {

    fun findByStoreIdAndWorkingDate(storeId: Long, workingDate: Long): Timetable

    fun findAfterCurrentDate(): Collection<Timetable>

    fun create(timetableDto: TimetableDto): Timetable
}