package com.dokhabackend.dokha.service.dictionary

import com.dokhabackend.dokha.converter.dictionary.StoreDtoToStoreEntityConverter
import com.dokhabackend.dokha.dto.dictionary.StoreDto
import com.dokhabackend.dokha.entity.dictionary.Store
import com.dokhabackend.dokha.repository.dictionary.StoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
@PreAuthorize("isAuthenticated()")
class StoreServiceImpl @Autowired constructor(
        private val storeRepository: StoreRepository,
        private val toEntityConverter: StoreDtoToStoreEntityConverter) : StoreService {

    @PreAuthorize("hasAuthority('ADMIN')")
    override fun create(store: StoreDto): Store = storeRepository.save(toEntityConverter.convert(store))

    @PreAuthorize("hasAuthority('ADMIN')")
    override fun update(store: StoreDto): Store = storeRepository.save(toEntityConverter.convert(store))

    @PreAuthorize("hasAuthority('ADMIN')")
    override fun delete(storeId: Long) = storeRepository.deleteById(storeId)

    override fun findById(id: Long): Store = storeRepository.findById(id).orElseThrow { Exception("not found") }

    override fun findAll(): Collection<Store> = storeRepository.findAll()

    override fun findByPlaceReservationId(placeId: Long): Store = storeRepository.findByPlaceReservationId(placeId).orElseThrow { throw IllegalStateException("not found") }

    override fun getPhotoByStoreId(storeId: Long): ByteArray = findById(storeId).photo


}