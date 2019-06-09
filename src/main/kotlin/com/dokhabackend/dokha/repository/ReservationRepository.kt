package com.dokhabackend.dokha.repository

import com.dokhabackend.dokha.entity.Reservation
import org.springframework.data.repository.CrudRepository


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

interface ReservationRepository : CrudRepository<Reservation, Long> {

}