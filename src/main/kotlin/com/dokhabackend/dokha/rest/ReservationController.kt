package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.converter.ReservationToReservationDtoConverter
import com.dokhabackend.dokha.core.RestResponse
import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.service.ReservationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


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

    @GetMapping
    fun findById(@RequestParam("id") id: Long): RestResponse<ReservationDto> {

        val reservation = reservationService.findById(id)

        return RestResponse(toDtoConverter.convert(reservation))
    }

//    @GetMapping
//    fun findByPlaceReservationId(@RequestParam("reservationId") reservationId: Long): RestResponse<Collection<ReservationDto>> {
//
//        val reservations = reservationService.findByPlaceReservationId(reservationId)
//
//        return RestResponse(toDtoConverter.convertToList(reservations))
//    }

}