package com.dokhabackend.dokha.dto


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/
data class ReservationDto(

        val id: Long,

        val placeReservationId: Long,

        val userId: Long,

        val reservationStartTime: Long,

        val reservationEndTime: Long,

        val closed: Boolean
)