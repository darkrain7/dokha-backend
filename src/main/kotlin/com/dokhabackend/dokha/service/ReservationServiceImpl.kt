package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.entity.Reservation
import com.dokhabackend.dokha.repository.ReservationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class ReservationServiceImpl
@Autowired constructor(val reservationRepository: ReservationRepository) : ReservationService {

    override fun findByPlaceReservationIdAndReserveTime(placeId: Long, reservationTime: Long): Reservation {
        TODO("not implemented")
    }

    override fun reserve(reservationDto: ReservationDto): Reservation {
        TODO("not implemented")
    }

    override fun findById(id: Long): Reservation =
            reservationRepository.findById(id).orElseThrow { throw IllegalStateException("not found") }

    override fun findByPlaceReservationId(placeId: Long): Collection<Reservation> {
        return reservationRepository.findByPlaceReservationId(placeId)
    }


}