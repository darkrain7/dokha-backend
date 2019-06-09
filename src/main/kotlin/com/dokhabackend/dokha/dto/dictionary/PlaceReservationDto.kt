package com.dokhabackend.dokha.dto.dictionary

import com.dokhabackend.dokha.dto.ReservationDto


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/
data class PlaceReservationDto(

        val id: Long?,

        val description: String,

        val seatsCount: Int,

        val storeId: Long,

        val reservations: Collection<ReservationDto>
)