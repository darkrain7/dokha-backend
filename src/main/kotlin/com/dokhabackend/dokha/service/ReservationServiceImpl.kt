package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.entity.Reservation
import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.repository.ReservationRepository
import com.dokhabackend.dokha.service.dictionary.PlaceReservationService
import com.dokhabackend.dokha.service.dictionary.StoreService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

private val logger = KotlinLogging.logger {}

@Service
class ReservationServiceImpl
@Autowired constructor(val reservationRepository: ReservationRepository,
                       val timetableService: TimetableService,
                       val placeReservationService: PlaceReservationService,
                       val userService: UserService,
                       val storeService: StoreService)
    : ReservationService {

    val halfHourInSec = 1800 //60 * 30 = 30min
    val tarifInSec = 5400 //60 * 60 * 1.5 = 1.5hours
    val hours24InSec = 86_400 // 60 * 60 * 24 = 24hours

    /**
     * Генерит все возможные варианты времени НАЧАЛА брони
     */
    override fun findFreeReservationStartTime(placeId: Long, reservationDate: LocalDate): Collection<Reservation> {
        val store = storeService.findByPlaceReservationId(placeId)

        //вытаскиваем расписание на текущий день
        val timetable = timetableService.findByStoreIdAndWorkingDate(store.id, reservationDate)

        val allReservesOnCurrentDay = findByPlaceIdAndTimetable(placeId, reservationDate, timetable)
                .filter { !it.closed }

        val range = timetable.startTime.toSecondOfDay()..(timetable.endTime.toSecondOfDay() - tarifInSec)

        val freeStartTime = mutableListOf<Reservation>()

        for (possibleStartTimeInSec in range step halfHourInSec) {

            if (haveIntersection(allReservesOnCurrentDay, possibleStartTimeInSec, possibleStartTimeInSec + tarifInSec)) continue

            val buildReservation = buildReservation(
                    placeId,
                    LocalDateTime.of(reservationDate, LocalTime.ofSecondOfDay(possibleStartTimeInSec.toLong())),
                    possibleStartTimeInSec,
                    possibleStartTimeInSec + tarifInSec)

            freeStartTime.add(buildReservation)
        }

        return freeStartTime
    }

    /**
     * Генерирует "свободные" варианты для брони
     */
    override fun findFreeReservation(placeId: Long, possibleStartDateTime: LocalDateTime): Collection<Reservation> {

        val store = storeService.findByPlaceReservationId(placeId)

        val truncedPossibleStartTime = possibleStartDateTime.toLocalDate()
        //вытаскиваем расписание на текущий день
        val timetable = timetableService.findByStoreIdAndWorkingDate(store.id, truncedPossibleStartTime)

        if (timetable.startTime > possibleStartDateTime.toLocalTime()) {
            logger.error { "Заведение работает только с ${timetable.startTime}" }
            throw IllegalArgumentException("Заведение работает только с ${timetable.startTime}")
        }

        //Вытаскиваем все брони на текущий день которые еще открыты
        val allReservesOnCurrentDay = findByPlaceIdAndTimetable(placeId, truncedPossibleStartTime, timetable)
                .filter { !it.closed }

        val freeReservation = mutableListOf<Reservation>()

        val possibleStartTimeOfSeconds = truncToHalfHour(possibleStartDateTime)

        val endTime: Int = getEndTime(timetable)
        val range = (possibleStartTimeOfSeconds + tarifInSec)..endTime

        for (possibleEndTimeOfSeconds in range step halfHourInSec) {

            if (haveIntersection(allReservesOnCurrentDay, possibleStartTimeOfSeconds, possibleEndTimeOfSeconds)) continue

            val buildReservation = buildReservation(
                    placeId,
                    possibleStartDateTime,
                    possibleStartTimeOfSeconds,
                    possibleEndTimeOfSeconds)

            freeReservation.add(buildReservation)
        }

        return freeReservation
    }

    /**
     * Бронирование места
     */
    override fun reserve(reservationDto: ReservationDto): Reservation {

        val store = storeService.findByPlaceReservationId(reservationDto.placeReservationId)

        //вытаскиваем расписание на текущий день
        val timetable = timetableService.findByStoreIdAndWorkingDate(store.id, reservationDto.reservationStartTime.toLocalDate())

        val allReservations = findByPlaceIdAndTimetable(reservationDto.placeReservationId, reservationDto.reservationStartTime.toLocalDate(), timetable)

        val start = timetable.startTime.toSecondOfDay()
        val end = getEndTime(timetable)

        for (possible in start..end step halfHourInSec) {

            val startReservation = reservationDto.reservationStartTime.toLocalTime().toSecondOfDay()
            val endReservation = reservationDto.reservationEndTime.toLocalTime().toSecondOfDay()

            val haveIntersection = haveIntersection(allReservations, startReservation, endReservation)

            if (haveIntersection)
                throw IllegalStateException("На данное время уже нельзя забронировать")
        }

        val details: User = SecurityContextHolder.getContext().authentication.principal as User
        val user = userService.findByLogin(details.username)

        val reservation = Reservation(
                placeReservation = placeReservationService.findById(reservationDto.placeReservationId),
                user = user,
                reservationStartTime = reservationDto.reservationStartTime,
                reservationEndTime = reservationDto.reservationEndTime,
                closed = false)

        val save = reservationRepository.save(reservation)
        logger.info { "Бронь успешно создана: user: ${save.user?.login}, place: ${save.placeReservation.id}, time: ${save.reservationStartTime}" }

        return save
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
        val end = generateEndTime(date, timetable)

        return reservationRepository.findByPlaceIdAndDateInterval(placeId, start, end)
    }

    private fun generateEndTime(date: LocalDate, timetable: Timetable): LocalDateTime =
            if (timetable.startTime > timetable.endTime)
                LocalDateTime.of(date, timetable.endTime).plusDays(1)
            else LocalDateTime.of(date, timetable.endTime)

    /**
     * Докинет сутки если заведение заканчивает работать на след сутки
     * LocalTime.of(00.00.00) == 0
     */
    private fun getEndTime(timetable: Timetable): Int {
        val endTime: Int = timetable.endTime.toSecondOfDay()

        return if (timetable.endTime < timetable.startTime)
            endTime + hours24InSec
        else endTime
    }

    override fun haveIntersection(allReservesOnCurrentDay: Collection<Reservation>,
                                  possibleStartTime: Int,
                                  possibleEndTime: Int): Boolean {
        return allReservesOnCurrentDay.any {

            val reserveStartTime = it.reservationStartTime.toLocalTime().toSecondOfDay()
            val reserveEndTime = it.reservationEndTime.toLocalTime().toSecondOfDay()
            val reserveTimeRange = reserveStartTime..reserveEndTime

            //время начала не находится в ренжде других броней(но может совпасть с концом другой брони)
            reserveTimeRange.containsWithoutBoundary(possibleStartTime)
                    //время конца не находится в ренжде других броней(но может совпасть с началом другой брони)
                    || reserveTimeRange.containsWithoutBoundary(possibleEndTime)
                    //реннж текущий брони не может содержать в себе ренжд другой брони
                    || (possibleStartTime..possibleEndTime).containsRange(reserveTimeRange)
        }
    }

    private fun truncToHalfHour(input: LocalDateTime): Int {

        val trunc = input.toLocalTime().truncatedTo(ChronoUnit.HOURS).toSecondOfDay()

        return if (input.minute >= 30)
            trunc + halfHourInSec
        else trunc
    }

    private fun buildReservation(placeId: Long,
                                 possibleStartDateTime: LocalDateTime,
                                 possibleStartTime: Int,
                                 possibleEndTime: Int): Reservation {

        return Reservation(
                placeReservation = placeReservationService.findById(placeId),
                user = null,
                reservationStartTime = LocalDateTime.of(possibleStartDateTime.toLocalDate(), LocalTime.ofSecondOfDay(possibleStartTime.toLong())),
                reservationEndTime = LocalDateTime.of(possibleStartDateTime.toLocalDate(), generateReservationEndTime(possibleEndTime)),
                closed = false
        )
    }

    /**
     * LocalTime не умеет инит из 86400 => перекидываем на начало
     */
    private fun generateReservationEndTime(possibleEndTime: Int): LocalTime =
            if (possibleEndTime == hours24InSec)
                LocalTime.ofSecondOfDay(0)
            else LocalTime.ofSecondOfDay(possibleEndTime.toLong())

}

private fun IntRange.containsEndInclusive(value: Int): Boolean = value > this.first && value <= this.last
private fun IntRange.containsStartInclusive(value: Int): Boolean = value >= this.first && value < this.last
private fun IntRange.containsWithoutBoundary(value: Int): Boolean = value > this.first && value < this.last
private fun IntRange.containsRange(value: IntRange): Boolean = this.start <= value.start && this.last >= value.last

private operator fun LocalDateTime.compareTo(value: Long): Int {
    return when {
        this > LocalDateTime.from(Instant.ofEpochSecond(value)) -> 1
        this < LocalDateTime.from(Instant.ofEpochSecond(value)) -> -1
        else -> 0
    }
}
