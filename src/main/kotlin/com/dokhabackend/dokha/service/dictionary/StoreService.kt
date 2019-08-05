package com.dokhabackend.dokha.service.dictionary

import com.dokhabackend.dokha.dto.dictionary.StoreDto
import com.dokhabackend.dokha.entity.dictionary.Store


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

interface StoreService {

    fun create(store: StoreDto): Store

    fun findById(id: Long): Store

    fun findAll(): Collection<Store>

    fun findByPlaceReservationId(placeId: Long): Store

    fun update(store: StoreDto): Store

    fun delete(storeId: Long): Unit

    fun getPhotoByStoreId(storeId: Long) : ByteArray
}