package com.dokhabackend.dokha.service.dictionary

import com.dokhabackend.dokha.entity.dictionary.Store
import java.util.*


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

interface StoreService {

    fun create(store: Store): Store

    fun findById(id: Long): Optional<Store>

    fun findAll(): Collection<Store>

    fun findByPlaceReservationId(placeId: Long): Store
}