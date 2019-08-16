package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.entity.Reservation
import com.dokhabackend.dokha.entity.Timetable
import java.time.LocalDate
import java.time.LocalDateTime


/**
 * Semenov A.E.
 * Created 14.06.2019
 *
 **/
interface ReservationService {

    fun findById(id: Long): Reservation

    fun findByPlaceReservationId(placeId: Long): Collection<Reservation>

    fun findByPlaceIdAndTimetableIsOpen(placeId: Long, date: LocalDate, timetable: Timetable): Collection<Reservation>

    fun findFreeReservationStartTime(placeId: Long, reservationDate: LocalDate): Collection<Reservation>

    fun findFreeReservation(placeId: Long, possibleStartDateTime: LocalDateTime): Collection<Reservation>

    fun reserve(reservationDto: ReservationDto): Reservation

    fun haveIntersection(allReservesOnCurrentDay: Collection<Reservation>, possibleStartTime: Int, possibleEndTime: Int): Boolean

    fun generatePreReserveComment(reservationDto: ReservationDto): String
}