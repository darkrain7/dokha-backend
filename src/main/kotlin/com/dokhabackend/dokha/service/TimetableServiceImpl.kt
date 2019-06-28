package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.converter.TimetableDtoToTimetableConverter
import com.dokhabackend.dokha.dto.TimetableDto
import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.repository.TimetableRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TimetableServiceImpl
@Autowired constructor(val timetableRepository: TimetableRepository,
                       val timetableConverter: TimetableDtoToTimetableConverter) : TimetableService {

    override fun findByStoreIdAndWorkingDate(storeId: Long, workingDate: Long): Timetable {
        val timetable = timetableRepository.findByStoreIdAndWorkingDate(storeId, workingDate)

        return timetable.orElseThrow { throw IllegalStateException("not found") }
    }

    override fun findAfterCurrentDate(): Collection<Timetable> {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        return timetableRepository.findByWorkingDateAfter(calendar.timeInMillis)

    }

    override fun create(timetableDto: TimetableDto): Timetable {

        val timetable = timetableConverter.convert(timetableDto)

        return timetableRepository.save(timetable)
    }
}