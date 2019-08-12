package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.entity.Reservation
import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.repository.ReservationRepository
import com.dokhabackend.dokha.service.dictionary.PlaceReservationService
import com.dokhabackend.dokha.service.dictionary.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*


@Service
class ReservationServiceImpl
@Autowired constructor(val reservationRepository: ReservationRepository,
                       val timetableService: TimetableService,
                       val placeReservationService: PlaceReservationService,
                       val storeService: StoreService)
    : ReservationService {


    private val tarif: Long = 5_400_000 //1000*60*60*1.5 = 1.5 часа. пока хардкод а то лень пиздец=\

    /**
     * Генерирует "свободные" варианты для брони
     */
    override fun findFreeReservation(placeId: Long, possibleStartTime: LocalDateTime): Collection<Reservation> {

        val store = storeService.findByPlaceReservationId(placeId)

        val date = possibleStartTime.toLocalDate()
        //вытаскиваем расписание на текущий день
        val timetable = timetableService.findByStoreIdAndWorkingDate(store.id, date)

        //Вытаскиваем все брони на текущий день
        val allReservesOnCurrentDay = findByPlaceIdAndTimetable(placeId, date, timetable)

        val stepHalfHour = Calendar.getInstance()
        stepHalfHour.set(Calendar.MINUTE, 30)
        val tarif = 5400 //60 * 60 * 1.5
        val step = 1800 //60 * 30

        val freeReservation = mutableListOf<Reservation>()

        val possibleStartTimeOfSeconds = possibleStartTime.toLocalTime().toSecondOfDay()
        val range = (possibleStartTimeOfSeconds + tarif)..timetable.endTime.toSecondOfDay()

        for (possibleEndTimeOfSeconds in range step step) {

            if (haveIntersection(allReservesOnCurrentDay, possibleStartTimeOfSeconds, possibleEndTimeOfSeconds)) continue

            freeReservation.add(buildReservation(placeId, possibleStartTime, possibleEndTimeOfSeconds))
        }

        return freeReservation
    }

    private fun haveIntersection(allReservesOnCurrentDay: Collection<Reservation>,
                                 possibleStartTime: Int,
                                 possibleEndTime: Int): Boolean {
        return allReservesOnCurrentDay.any {

            val reserveStartTime = it.reservationStartTime.toLocalTime().toSecondOfDay()
            val reserveEndTime = it.reservationEndTime.toLocalTime().toSecondOfDay()
            val reserveTimeRange = reserveStartTime..reserveEndTime

            //время начала мб = времени конца брони
            reserveTimeRange.containsEndInclusive(possibleStartTime)
                    //время конца мб = времени начала брони
                    && reserveTimeRange.containsStartInclusive(possibleEndTime)
                    //реннж текущий брони не может содержать в себе ренжд другой брони
                    && !(possibleStartTime..possibleEndTime).containsRange(reserveTimeRange)
        }
    }

    override fun reserve(reservationDto: ReservationDto): Reservation {

        val reservationsInCurrentDate = reservationRepository.findByPlaceIdAndDateInterval(
                reservationDto.placeReservationId,
                reservationDto.reservationStartTime,
                reservationDto.reservationEndTime)

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
    override fun findByPlaceIdAndTimetable(placeId: Long, date: LocalDate, timetable: Timetable): Collection<Reservation> {

        val start = LocalDateTime.of(date, timetable.startTime)
        val end = LocalDateTime.of(date, timetable.endTime)

        return reservationRepository.findByPlaceIdAndDateInterval(placeId, start, end)
    }

    private fun buildReservation(placeId: Long,
                                 possibleStartTime: LocalDateTime,
                                 possibleEndTime: Int): Reservation {

        val reservation = Reservation(
                placeReservation = placeReservationService.findById(placeId),
                user = null,
                reservationStartTime = possibleStartTime,
                reservationEndTime = LocalDateTime.of(possibleStartTime.toLocalDate(), LocalTime.ofSecondOfDay(possibleEndTime.toLong())),
                closed = false
        )

        return reservation
    }

}

private fun IntRange.containsEndInclusive(value: Int): Boolean = value > this.first && value <= this.last

private fun IntRange.containsStartInclusive(value: Int): Boolean = value >= this.first && value < this.last

private fun IntRange.containsRange(value: IntRange): Boolean {
    return this.start <= value.start && this.last >= value.last
}


private operator fun LocalDateTime.compareTo(value: Long): Int {
    return when {
        this > LocalDateTime.from(Instant.ofEpochSecond(value)) -> 1
        this < LocalDateTime.from(Instant.ofEpochSecond(value)) -> -1
        else -> 0
    }
}
