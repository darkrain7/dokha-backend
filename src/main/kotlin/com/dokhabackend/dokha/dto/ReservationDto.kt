package com.dokhabackend.dokha.dto

import java.time.LocalDateTime


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/
data class ReservationDto(

        val id: Long,

        val placeReservationId: Long,

        val userId: Long,

        val reservationStartTime: LocalDateTime,

        val reservationEndTime: LocalDateTime,

        val closed: Boolean
)