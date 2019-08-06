package com.dokhabackend.dokha.dto.dictionary


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/
data class PlaceReservationDto(

        val id: Long = 0,

        val description: String,

        val seatsCount: Int,

        val haveGamingConsole : Boolean,

        val storeId: Long
)