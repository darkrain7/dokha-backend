package com.dokhabackend.dokha.repository

import com.dokhabackend.dokha.entity.Reserve
import org.springframework.data.repository.CrudRepository


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

interface ReserveRepository : CrudRepository<Reserve, Long> {

}