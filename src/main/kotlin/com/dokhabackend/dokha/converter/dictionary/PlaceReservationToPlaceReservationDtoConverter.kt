package com.dokhabackend.dokha.converter.dictionary

import com.dokhabackend.dokha.converter.AbstractConverter
import com.dokhabackend.dokha.dto.dictionary.PlaceReservationDto
import com.dokhabackend.dokha.entity.dictionary.PlaceReservation
import org.springframework.stereotype.Component


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/

@Component
class PlaceReservationToPlaceReservationDtoConverter : AbstractConverter<PlaceReservation, PlaceReservationDto>() {

    override fun convert(fromObject: PlaceReservation): PlaceReservationDto =
            PlaceReservationDto(
                    id = fromObject.id,
                    description = fromObject.description,
                    seatsCount = fromObject.seatsCount,
                    haveGamingConsole = fromObject.haveGamingConsole,
                    storeId = fromObject.store.id
            )
}