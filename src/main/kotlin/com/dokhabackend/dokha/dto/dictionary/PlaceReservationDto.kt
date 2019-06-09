package com.dokhabackend.dokha.dto.dictionary


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/
data class PlaceReservationDto(

        val id: Long?,

        val description: String,

        val seatsCount: Int,

        val storeId: Long
)