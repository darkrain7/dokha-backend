package com.dokhabackend.dokha.converter.dictionary

import com.dokhabackend.dokha.converter.AbstractConverter
import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.entity.Reservation
import com.dokhabackend.dokha.service.dictionary.PlaceReservationService
import com.dokhabackend.dokha.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/

@Component
class ReservationDtoToReservationEntityConverter
@Autowired constructor(val placeReservationService: PlaceReservationService,
                       val userService: UserService)
    : AbstractConverter<ReservationDto, Reservation>() {


    override fun convert(fromObject: ReservationDto): Reservation =
            Reservation(
                    id = fromObject.id,
                    placeReservation = placeReservationService.findById(fromObject.placeReservationId),
                    user = userService.findById(fromObject.userId),
                    reservationTime = fromObject.reservationTime,
                    timetable = null
            )
}