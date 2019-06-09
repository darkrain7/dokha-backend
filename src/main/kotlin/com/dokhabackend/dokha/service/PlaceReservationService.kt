package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.entity.dictionary.PlaceReservation


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/
interface PlaceReservationService {

    fun findById(id: Long): PlaceReservation

    fun findAll(): Collection<PlaceReservation>

    fun findByStoreId(storeId: Long): Collection<PlaceReservation>
}