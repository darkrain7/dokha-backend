package com.dokhabackend.dokha.repository.dictionary

import com.dokhabackend.dokha.entity.dictionary.Store
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

@Repository
interface StoreRepository : CrudRepository<Store, Long> {

    override fun findAll(): Collection<Store>

    @Query(value = "SELECT * " +
            "FROM s_store s " +
            "LEFT JOIN s_place_reservation pr ON s.id = pr.store_id " +
            "  WHERE pr.id = :placeId", nativeQuery = true)
    fun findByPlaceReservationId(@Param("placeId") placeId: Long): Optional<Store>
}