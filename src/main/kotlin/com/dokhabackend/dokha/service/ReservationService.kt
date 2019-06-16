package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.entity.Reservation


/**
 * Semenov A.E.
 * Created 14.06.2019
 *
 **/
interface ReservationService {

    fun findById(id: Long): Reservation

    fun findByPlaceReservationId(placeId: Long): Collection<Reservation>

    fun findByPlaceReservationIdAndReserveTime(placeId: Long, reservationTime: Long): Reservation

    fun reserve(reservationDto: ReservationDto): Reservation
}