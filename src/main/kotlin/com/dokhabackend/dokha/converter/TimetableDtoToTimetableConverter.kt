package com.dokhabackend.dokha.converter

import com.dokhabackend.dokha.dto.TimetableDto
import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.service.dictionary.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


/**
 * Semenov A.E.
 * Created 10.05.2019
 *
 **/
@Component
class TimetableDtoToTimetableConverter
@Autowired constructor(val storeService: StoreService) : AbstractConverter<TimetableDto, Timetable>() {

    override fun convert(fromObject: TimetableDto): Timetable =
            Timetable(
                    id = fromObject.id!!,
                    startDate = fromObject.startDate,
                    endDate = fromObject.endDate,
                    workingDate = fromObject.workingDate,
                    workingDay = fromObject.workingDay,
                    store = storeService.findById(fromObject.storeId)
            )
}