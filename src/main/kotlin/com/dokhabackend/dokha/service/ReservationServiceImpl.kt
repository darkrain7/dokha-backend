package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.util.Util
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
                       val storeService: StoreService)
    : ReservationService {

    /**
     * Генерирует "свободные" варианты для брони
     */
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

        for (time in timetable.startTime..timetable.endTime step halfHour.timeInMillis) {

            if (allReservesOnCurrentDay.any { it.reservationTime == time }) continue

            freeReservation.add(buildReservation(placeId, timetable, time))
        }

        return freeReservation
    }

    override fun reserve(reservationDto: ReservationDto): Reservation {

        val startTimeOfDateCalendar = getStartTimeOfDateCalendar(reservationDto.reservationTime)

        val endTimeOfCalendar = getEndTimeOfCalendar(reservationDto.reservationTime)

        val reservationsInCurrentDate = reservationRepository.findByPlaceIdAndDateInterval(
                reservationDto.placeReservationId,
                startTimeOfDateCalendar.timeInMillis,
                endTimeOfCalendar.timeInMillis)

    return reservationsInCurrentDate.first()
    }

    private fun getStartTimeOfDateCalendar(date: Long): Calendar {
        val startTimeCalendar = Calendar.getInstance()

        startTimeCalendar.time = Date(date)
        startTimeCalendar.set(Calendar.HOUR_OF_DAY, 0)
        startTimeCalendar.set(Calendar.MINUTE, 0)

        return startTimeCalendar
    }

    private fun getEndTimeOfCalendar(date: Long): Calendar {

        val endTimeCalendar = Calendar.getInstance()

        endTimeCalendar.time = Date(date)
        endTimeCalendar.set(Calendar.HOUR_OF_DAY, 22)
        endTimeCalendar.set(Calendar.MINUTE, 59)
        endTimeCalendar.set(Calendar.SECOND, 59)

        return endTimeCalendar
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

    private fun buildReservation(placeId: Long, timetable: Timetable, time: Long): Reservation {
        return Reservation(
                placeReservation = placeReservationService.findById(placeId),
                user = null,
                timetable = timetable,
                reservationTime = time
        )
    }
}
