package com.dokhabackend.dokha.dto

import java.sql.Timestamp


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/
data class ReservationDto(

        val id: Long,

        val placeReservationId: Long,

        val user_id: Long,

        val reservationTime: Timestamp
)