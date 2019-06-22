package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.Util.Util
import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.entity.Reservation
import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.repository.ReservationRepository
import com.dokhabackend.dokha.service.dictionary.PlaceReservationService
import com.dokhabackend.dokha.service.dictionary.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class ReservationServiceImpl
@Autowired constructor(val reservationRepository: ReservationRepository,
                       val timetableService: TimetableService,
                       val placeReservationService: PlaceReservationService,
                       val storeService: StoreService) : ReservationService {

    override fun findByPlaceReservationIdAndReserveTime(placeId: Long, reservationTime: Long): Reservation {
        TODO("not implemented")
    }

    override fun findFreeReservation(placeId: Long, reservationDate: Long): Collection<Reservation> {

        val store = storeService.findByPlaceReservationId(placeId)

        val truncDate = Util().truncDate(reservationDate)

        //вытаскиваем расписание на текущий день
        val timetable = timetableService.findByStoreIdAndWorkingDate(store.id, truncDate)

        //Вытаскиваем все брони на текущий день
        val allReservesOnCurrentDay = findByPlaceIdAndDate(placeId, truncDate)

        val halfHour = Calendar.getInstance()
        halfHour.set(Calendar.MINUTE, 30)

        val freeReservation = Collections.emptyList<Reservation>()

        for (time in timetable.startDate..timetable.endDate step halfHour.timeInMillis) {

            if (allReservesOnCurrentDay.any { it.reservationTime == time }) continue

            freeReservation.add(buildReservation(placeId, timetable, time))
        }

        return freeReservation
    }

    private fun buildReservation(placeId: Long, timetable: Timetable, time: Long): Reservation {
        return Reservation(
                placeReservation = placeReservationService.findById(placeId),
                user = null,
                timetable = timetable,
                reservationTime = time
        )
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