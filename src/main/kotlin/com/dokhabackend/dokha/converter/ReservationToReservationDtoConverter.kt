package com.dokhabackend.dokha.converter

import com.dokhabackend.dokha.dto.ReservationDto
import com.dokhabackend.dokha.entity.Reservation
import org.springframework.stereotype.Component


/**
 * Semenov A.E.
 * Created 09.06.2019
 *
 **/

@Component
class ReservationToReservationDtoConverter : AbstractConverter<Reservation, ReservationDto>() {

    override fun convert(fromObject: Reservation): ReservationDto =
            ReservationDto(
                    id = fromObject.id,
                    placeReservationId = fromObject.placeReservation.id,
                    userId = if (fromObject.user?.id == null) 0 else fromObject.user.id,
                    reservationStartTime = fromObject.reservationStartTime,
                    reservationEndTime = fromObject.reservationEndTime,
                    closed = fromObject.closed
            )
}