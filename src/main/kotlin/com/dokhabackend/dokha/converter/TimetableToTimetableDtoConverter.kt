package com.dokhabackend.dokha.converter

import com.dokhabackend.dokha.dto.TimetableDto
import com.dokhabackend.dokha.entity.Timetable
import org.springframework.stereotype.Component


/**
 * Semenov A.E.
 * Created 10.05.2019
 *
 **/
@Component
class TimetableToTimetableDtoConverter : AbstractConverter<Timetable, TimetableDto>() {
    override fun convert(fromObject: Timetable): TimetableDto =
            TimetableDto(
                    id = fromObject.id,
                    startDate = fromObject.startTime,
                    endDate = fromObject.endTime,
                    workingDate = fromObject.workingDate,
                    workingDay = fromObject.workingDay,
                    storeId = fromObject.store.id
            )
}