package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.converter.ReservationToReservationDtoConverter
import com.dokhabackend.dokha.core.RestResponse
import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.service.ReservationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.web.bind.annotation.*
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalDateTime.ofEpochSecond
import java.time.LocalTime
import java.time.ZoneOffset
import java.util.*


/**
 * Semenov A.E.
 * Created 14.06.2019
 *
 **/

@RestController
@RequestMapping(value = ["/reservation"])
class ReservationController
@Autowired constructor(val reservationService: ReservationService,
                       val toDtoConverter: ReservationToReservationDtoConverter) {

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): RestResponse<ReservationDto> {

        val reservation = reservationService.findById(id)

        return RestResponse(toDtoConverter.convert(reservation))
    }

    @GetMapping("/findFree/startTime/{placeReservationId}/{reservationDate}")
    fun findFreeReservationStartTime(@PathVariable("placeReservationId") placeReservationId: Long,
                                     @PathVariable("reservationDate") reservationDate: Long)
            : RestResponse<Collection<ReservationDto>> {

        val calendar = Calendar.getInstance()
        calendar.time = Date(reservationDate)

        val ofEpochSecond = ofEpochSecond(reservationDate, 0, ZoneOffset.UTC).toLocalDate()
        val localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))

        val reservationStartTime = reservationService.findFreeReservationStartTime(placeReservationId, ofEpochSecond)

        return RestResponse(toDtoConverter.convertToList(reservationStartTime))
    }

    @GetMapping("/findFree/{placeReservationId}/{reservationStartTime}")
    fun findFreeReservation(@PathVariable("placeReservationId") placeReservationId: Long,
                            @PathVariable("reservationStartTime") reservationStartTime: Long)
            : RestResponse<Collection<ReservationDto>> {

        val calendar = Calendar.getInstance()
        calendar.time = Date(reservationStartTime)

        val localDate = LocalDate.of(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH))
        val localTime = LocalTime.of(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), calendar.get(Calendar.SECOND))
        val localDateTime = LocalDateTime.of(localDate, localTime)

        val reservations = reservationService.findFreeReservation(placeReservationId, localDateTime)

        return RestResponse(toDtoConverter.convertToList(reservations))
    }

    @GetMapping("/findFree/v2/startTime/{placeReservationId}/{reservationDate}")
    fun findFreeReservationStartTimeV2(@PathVariable("placeReservationId") placeReservationId: Long,
                                       @PathVariable("reservationDate")
                                       @DateTimeFormat(pattern = "yyyy-MM-dd") reservationDate: LocalDate)
            : RestResponse<Collection<ReservationDto>> {

        val reservationStartTime = reservationService.findFreeReservationStartTime(placeReservationId, reservationDate)

        return RestResponse(toDtoConverter.convertToList(reservationStartTime))
    }

    @GetMapping("/findFree/v2/{placeReservationId}/{reservationStartTime}")
    fun findFreeReservationV2(@PathVariable("placeReservationId") placeReservationId: Long,
                              @PathVariable("reservationStartTime")
                              @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") reservationStartTime: LocalDateTime)
            : RestResponse<Collection<ReservationDto>> {

        val findFreeReservation = reservationService.findFreeReservation(placeReservationId, reservationStartTime)

        return RestResponse(toDtoConverter.convertToList(findFreeReservation))
    }

    @PostMapping("/reserve")
    fun reserve(@RequestBody reservationDto: ReservationDto): RestResponse<ReservationDto> {

        val reserve = reservationService.reserve(reservationDto)

        return RestResponse(toDtoConverter.convert(reserve))
    }

}