package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.entity.Reservation
import com.dokhabackend.dokha.repository.ReservationRepository
import com.dokhabackend.dokha.service.dictionary.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReservationServiceImpl
@Autowired constructor(val reservationRepository: ReservationRepository,
                       val timetableService: TimetableService,
                       val storeService: StoreService) : ReservationService {

    override fun findByPlaceReservationIdAndReserveTime(placeId: Long, reservationTime: Long): Reservation {
        TODO("not implemented")
    }

    override fun findFreeReservation(placeId: Long, reservationDate: Long): Collection<Reservation> {

        val store = storeService.findByPlaceReservationId(placeId)

        val calendar: Calendar = Calendar.getInstance()
        calendar.time = Date(reservationDate)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        //вытаскиваем расписание на текущий день
        val timetable = timetableService.findByStoreIdAndWorkingDate(store.id, calendar.timeInMillis)


        val allReservesOnCurrentDay = findByPlaceIdAndDate(placeId, calendar.timeInMillis)

        return Collections.emptyList()
    }

    override fun reserve(reservationDto: ReservationDto): Reservation {
        TODO("not implemented")
    }

    override fun findById(id: Long): Reservation =
            reservationRepository.findById(id).orElseThrow { throw IllegalStateException("not found") }

    override fun findByPlaceReservationId(placeId: Long): Collection<Reservation> =
            reservationRepository.findByPlaceReservationId(placeId)


    /**
     * Достает все брони по столу за указанный день
     */
    override fun findByPlaceIdAndDate(placeId: Long, date: Long): Collection<Reservation> =
            reservationRepository.findByPlaceIdAndDate(placeId, date)


}