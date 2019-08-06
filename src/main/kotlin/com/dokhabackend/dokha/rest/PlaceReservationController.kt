package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.converter.dictionary.PlaceReservationToPlaceReservationDtoConverter
import com.dokhabackend.dokha.core.RestResponse
import com.dokhabackend.dokha.dto.dictionary.PlaceReservationDto
import com.dokhabackend.dokha.service.dictionary.PlaceReservationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/

@RestController
@RequestMapping(value = ["/placeReservation"])
class PlaceReservationController
@Autowired constructor(val placeReservationService: PlaceReservationService,
                       val toDtoConverter: PlaceReservationToPlaceReservationDtoConverter) {

    @GetMapping("/{id}")
    fun findById(@PathVariable("id") id: Long): RestResponse<PlaceReservationDto> {
        val placeReservation = placeReservationService.findById(id)

        return RestResponse(toDtoConverter.convert(placeReservation))
    }

    @GetMapping(value = ["/findAll"])
    fun findAll(): RestResponse<Collection<PlaceReservationDto>> {
        val placeReservations = placeReservationService.findAll()

        return RestResponse(toDtoConverter.convertToList(placeReservations))
    }

    @GetMapping(value = ["/storeId/{storeId}"])
    fun findByStoreId(@PathVariable("storeId") storeId: Long): RestResponse<Collection<PlaceReservationDto>> {
        val placeReservation = placeReservationService.findByStoreId(storeId)

        return RestResponse(toDtoConverter.convertToList(placeReservation))
    }
}