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
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.*
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
    val halfHour = 30L
    val tarifInSec = 5400 //60 * 60 * 1.5 = 1.5hours
    val hours24InSec = 86_400 // 60 * 60 * 24 = 24hours

    /**
     * Генерит все возможные варианты времени НАЧАЛА брони
     */
    override fun findFreeReservationStartTime(placeId: Long, reservationDate: LocalDate): Collection<Reservation> {
        val store = storeService.findByPlaceReservationId(placeId)

        //вытаскиваем расписание на текущий день
        val timetable = timetableService.findByStoreWorkingDateAndWorked(store.id, reservationDate)

        val allReservesOnCurrentDay = findByPlaceIdAndTimetableIsOpen(placeId, reservationDate, timetable)

        val startTime = timetable.startTime.toSecondOfDay()
        val endTime = getEndTime(timetable) - tarifInSec
        val range = startTime..endTime

        val freeStartTime = mutableListOf<Reservation>()

        for (possibleStartTimeInSec in range step halfHourInSec) {

            if (haveIntersection(allReservesOnCurrentDay,
                            possibleStartTimeInSec,
                            possibleStartTimeInSec + tarifInSec))
                continue

            val buildReservation = buildReservation(
                    placeId,
                    reservationDate,
                    possibleStartTimeInSec,
                    possibleStartTimeInSec + tarifInSec)

            freeStartTime.add(buildReservation)
        }

        return freeStartTime
                .filter { it.reservationStartTime > LocalDateTime.now(ZoneOffset.systemDefault()) }
    }

    /**
     * Генерирует "свободные" варианты для брони для даты начала
     */
    override fun findFreeReservation(placeId: Long, possibleStartDateTime: LocalDateTime): Collection<Reservation> {

        val store = storeService.findByPlaceReservationId(placeId)

        val possibleStartDate = possibleStartDateTime.toLocalDate()
        //вытаскиваем расписание на текущий день
        val timetable = timetableService.findByStoreWorkingDateAndWorked(store.id, possibleStartDate)

        if (timetable.startTime.toSecondOfDay() > possibleStartDateTime.toLocalTime().toSecondOfDay()
                || getEndTime(timetable) < possibleStartDateTime.toLocalTime().toSecondOfDay()) {
            logger.error { "Заведение работает только с ${timetable.startTime} до ${timetable.endTime}" }
            throw IllegalArgumentException("Заведение работает только с ${timetable.startTime} до ${timetable.endTime}")
        }

        //Вытаскиваем все брони на текущий день которые еще открыты
        val allReservesOnCurrentDay = findByPlaceIdAndTimetableIsOpen(placeId, possibleStartDate, timetable)

        val possibleStartTimeOfSeconds = truncToHalfHour(possibleStartDateTime).toLocalTime().toSecondOfDay()

        val endTime: Int = getEndTime(timetable)
        val range = (possibleStartTimeOfSeconds + tarifInSec)..endTime

        val freeReservation = mutableListOf<Reservation>()

        for (possibleEndTimeOfSeconds in range step halfHourInSec) {

            if (haveIntersection(allReservesOnCurrentDay,
                            possibleStartTimeOfSeconds,
                            possibleEndTimeOfSeconds))
                continue

            val buildReservation = buildReservation(
                    placeId,
                    possibleStartDateTime.toLocalDate(),
                    possibleStartTimeOfSeconds,
                    possibleEndTimeOfSeconds)

            freeReservation.add(buildReservation)
        }

        return freeReservation
    }

    override fun findCurrentActiveReservations(storeId: Long): Collection<Reservation> =
            reservationRepository.findByStoreIdAndDateInterval(storeId, LocalDateTime.now(), LocalDateTime.now())


    override fun closeReservation(id: Long): Reservation {
        val reservation = findById(id)

        val closedReservation = reservation.copy(reservationEndTime = LocalDateTime.now())

        return reservationRepository.save(closedReservation)
    }

    override fun placeReservationStateOnCurrentTime(storeId: Long): Collection<Reservation> =
            placeReservationService.findByStoreId(storeId)
                    .flatMap { reservationRepository.findByPlaceReservationId(it.id) }
                    .filter { it.reservationStartTime >= LocalDateTime.now() && it.reservationEndTime < LocalDateTime.now() }

    override fun generatePreReserveComment(reservationDto: ReservationDto): String {

        val start = reservationDto.reservationStartTime.toEpochSecond(ZoneOffset.UTC)
        val end = reservationDto.reservationEndTime.toEpochSecond(ZoneOffset.UTC)

        val hookahCount = BigDecimal(end - start)
                .divide(BigDecimal(tarifInSec), RoundingMode.CEILING)

        val hookah: String
        hookah = when {
            hookahCount.toInt() < 1 -> "???"
            hookahCount.toInt() == 1 -> "кальяна"
            else -> "кальянов"
        }

        return "Данное время предусматривает заказ минимум $hookahCount $hookah"
    }

    /**
     * Бронирование места
     */
    override fun reserve(reservationDto: ReservationDto): Reservation {

        val store = storeService.findByPlaceReservationId(reservationDto.placeReservationId)

        //вытаскиваем расписание на текущий день
        val timetable = timetableService.findByStoreWorkingDateAndWorked(store.id, reservationDto.reservationStartTime.toLocalDate())

        val allReservations = findByPlaceIdAndTimetableIsOpen(reservationDto.placeReservationId, reservationDto.reservationStartTime.toLocalDate(), timetable)

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
    override fun findByPlaceIdAndTimetableIsOpen(placeId: Long, date: LocalDate, timetable: Timetable): Collection<Reservation> {

        val start = LocalDateTime.of(date, timetable.startTime)
        val end = generateEndTime(date, timetable)

        return reservationRepository.findByPlaceIdAndDateInterval(placeId, start, end)
                .filter { !it.closed }
    }

    private fun generateEndTime(date: LocalDate, timetable: Timetable): LocalDateTime =
            if (timetable.startTime > timetable.endTime)
                LocalDateTime.of(date, timetable.endTime).plusDays(1)
            else LocalDateTime.of(date, timetable.endTime)

    /**
     * Докинет сутки если заведение заканчивает работать на след сутки
     * LocalTime.of(00.00.00) == 0
     */
    private fun getEndTime(startTime: LocalTime, endTime: LocalTime): Int {
        return if (endTime < startTime)
            endTime.toSecondOfDay() + hours24InSec
        else endTime.toSecondOfDay()
    }

    private fun getEndTime(timetable: Timetable): Int = getEndTime(timetable.startTime, timetable.endTime)
    private fun getEndTime(reservation: Reservation): Int {
        val startTime = reservation.reservationStartTime.toLocalTime()
        val endTime = reservation.reservationEndTime.toLocalTime()

        return getEndTime(startTime, endTime)
    }

    override fun haveIntersection(allReservesOnCurrentDay: Collection<Reservation>,
                                  possibleStartTime: Int,
                                  possibleEndTime: Int): Boolean {
        return allReservesOnCurrentDay.any {

            val reserveStartTime = it.reservationStartTime.toLocalTime().toSecondOfDay()
            val reserveEndTime = getEndTime(it)
            val reserveTimeRange = reserveStartTime..reserveEndTime

            //время начала не находится в ренжде других броней(но может совпасть с концом другой брони)
            reserveTimeRange.containsWithoutBoundary(possibleStartTime)
                    //время конца не находится в ренжде других броней(но может совпасть с началом другой брони)
                    || reserveTimeRange.containsWithoutBoundary(possibleEndTime)
                    //реннж текущий брони не может содержать в себе ренжд другой брони
                    || (possibleStartTime..possibleEndTime).containsRange(reserveTimeRange)
        }
    }

    private fun buildReservation(placeId: Long,
                                 possibleStartDate: LocalDate,
                                 possibleStartTime: Int,
                                 possibleEndTime: Int): Reservation {

        return Reservation(
                placeReservation = placeReservationService.findById(placeId),
                user = null,
                reservationStartTime = generateReservationTime(possibleStartDate, possibleStartTime),
                reservationEndTime = generateReservationTime(possibleStartDate, possibleEndTime),
                closed = false
        )
    }

    private fun truncToHalfHour(input: LocalDateTime): LocalDateTime {
        val trunc = input.toLocalTime().truncatedTo(ChronoUnit.HOURS)

        return if (input.minute >= halfHour)
            LocalDateTime.of(input.toLocalDate(), trunc.plusMinutes(halfHour))
        else LocalDateTime.of(input.toLocalDate(), trunc)
    }

    /**
     * Прибавляет сутки при необходимости
     */
    private fun generateReservationTime(possibleStartDate: LocalDate, possibleEndTime: Int): LocalDateTime {
        return if (possibleEndTime >= hours24InSec)
            LocalDateTime.of(possibleStartDate.plusDays(1), LocalTime.ofSecondOfDay((possibleEndTime - hours24InSec).toLong()))
        else LocalDateTime.of(possibleStartDate, LocalTime.ofSecondOfDay(possibleEndTime.toLong()))
    }
}

private fun IntRange.containsWithoutBoundary(value: Int): Boolean = value > this.first && value < this.last
private fun IntRange.containsRange(value: IntRange): Boolean = this.start <= value.start && this.last >= value.last

private operator fun LocalDateTime.compareTo(value: Long): Int {
    return when {
        this > LocalDateTime.from(Instant.ofEpochSecond(value)) -> 1
        this < LocalDateTime.from(Instant.ofEpochSecond(value)) -> -1
        else -> 0
    }
}
