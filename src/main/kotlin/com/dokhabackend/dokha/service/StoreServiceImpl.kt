package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.entity.dictionary.Store
import com.dokhabackend.dokha.repository.dictionary.StoreRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
@PreAuthorize("isAuthenticated()")
class StoreServiceImpl
@Autowired constructor(private val storeRepository: StoreRepository) : StoreService {

    @PreAuthorize("hasAuthority('ADMIN')")
    override fun create(store: Store): Store = storeRepository.save(store)

    override fun findById(id: Long) = storeRepository.findById(id)

    override fun findAll(): Collection<Store> = storeRepository.findAll()
}