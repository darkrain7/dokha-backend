package com.dokhabackend.dokha.repository.dictionary

import com.dokhabackend.dokha.entity.dictionary.Store
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

@Repository
interface StoreRepository : CrudRepository<Store, Long> {

}