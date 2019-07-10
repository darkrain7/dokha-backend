package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.converter.TimetableDtoToTimetableConverter
import com.dokhabackend.dokha.dto.TimetableDto
import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.entity.dictionary.DayOfWeek
import com.dokhabackend.dokha.entity.dictionary.TimetableConfig
import com.dokhabackend.dokha.repository.TimetableRepository
import com.dokhabackend.dokha.service.dictionary.StoreService
import com.dokhabackend.dokha.service.dictionary.TimetableConfigService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TimetableServiceImpl
@Autowired constructor(val timetableRepository: TimetableRepository,
                       val timetableConfigService: TimetableConfigService,
                       val timetableConverter: TimetableDtoToTimetableConverter,
                       val storeService: StoreService) : TimetableService {

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

    override fun generateDefaultTimetable(day: Long, storeId: Long): Timetable {

        val calendar = Calendar.getInstance()
        calendar.time = Date(day)
        val dayOfWeak = calendar.get(Calendar.DAY_OF_WEEK)

        //TODO("Адеквантный мапер")

        val config = timetableConfigService.findByDayOfWeekAndStoreId(DayOfWeek(dayOfWeak.toLong(), name = "some name"), storeId)

        return generateTimetableByConfig(config, day, storeId)
    }

    override fun create(timetableDto: TimetableDto): Timetable {

        val timetable = timetableConverter.convert(timetableDto)

        return timetableRepository.save(timetable)
    }

    override fun create(timetable: Timetable): Timetable {
        //TODO ПРОВЕРКИ
        return timetableRepository.save(timetable)
    }

    override fun findMaxWorkingDateByStoreId(storeId: Long): Timetable? =
            timetableRepository.findMaxWorkingDateByStoreId(storeId)

    override fun findAll(): Collection<Timetable> =
            timetableRepository.findAll()

    private fun generateTimetableByConfig(config: TimetableConfig, day: Long, storeId: Long) = Timetable(
            startTime = config.startTime,
            endTime = config.endTime,
            workingDay = true,
            workingDate = day,
            store = storeService.findById(storeId))
}
