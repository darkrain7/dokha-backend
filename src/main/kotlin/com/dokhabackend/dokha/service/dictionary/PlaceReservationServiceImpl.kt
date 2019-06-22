package com.dokhabackend.dokha.service.dictionary

import com.dokhabackend.dokha.entity.dictionary.PlaceReservation
import com.dokhabackend.dokha.repository.dictionary.PlaceReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service


@Service
@PreAuthorize("isAuthenticated()")
class PlaceReservationServiceImpl
@Autowired constructor(val placeReservationRepository: PlaceReservationRepository) : PlaceReservationService {

    override fun findById(id: Long): PlaceReservation =
            placeReservationRepository.findById(id).orElseThrow { IllegalStateException("place not found") }

    override fun findAll(): Collection<PlaceReservation> = placeReservationRepository.findAll()

    override fun findByStoreId(storeId: Long): Collection<PlaceReservation> = placeReservationRepository.findByStoreId(storeId)
}