package com.dokhabackend.dokha.repository.dictionary

import com.dokhabackend.dokha.entity.dictionary.PlaceReservation
import org.springframework.data.repository.CrudRepository


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

interface PlaceReservationRepository : CrudRepository<PlaceReservation, Long> {
    override fun findAll(): Collection<PlaceReservation>

    fun findByStoreId(id: Long): Collection<PlaceReservation>
}