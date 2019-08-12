package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.converter.TimetableDtoToTimetableConverter
import com.dokhabackend.dokha.dto.TimetableDto
import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.entity.dictionary.TimetableConfig
import com.dokhabackend.dokha.repository.TimetableRepository
import com.dokhabackend.dokha.service.dictionary.StoreService
import com.dokhabackend.dokha.service.dictionary.TimetableConfigService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.*

private val logger = KotlinLogging.logger {}

@Service
class TimetableServiceImpl
@Autowired constructor(val timetableRepository: TimetableRepository,
                       val timetableConfigService: TimetableConfigService,
                       val timetableConverter: TimetableDtoToTimetableConverter,
                       val storeService: StoreService) : TimetableService {

    override fun findByStoreIdAndWorkingDate(storeId: Long, workingDate: LocalDate): Timetable {
        val timetable = timetableRepository.findByStoreIdAndWorkingDate(storeId, workingDate)

        return timetable.orElseThrow { throw IllegalStateException("not found") }
    }

    override fun isExistByStoreIdAndWorkingDate(storeId: Long, workingDate: LocalDate): Boolean =
            timetableRepository.findByStoreIdAndWorkingDate(storeId, workingDate).isPresent


    override fun findAfterCurrentDate(): Collection<Timetable> {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        return timetableRepository.findByWorkingDateAfter(calendar.timeInMillis)

    }

    override fun generateDefaultTimetable(day: LocalDate, storeId: Long): Timetable {

        val config = timetableConfigService.findByDayOfWeekAndStoreId(day.dayOfWeek.value.toLong(), storeId)

        return generateTimetableByConfig(config, day, storeId)
    }

    override fun create(timetableDto: TimetableDto): Timetable {

        val timetable = timetableConverter.convert(timetableDto)

        return timetableRepository.save(timetable)
    }

    override fun create(timetable: Timetable): Timetable {
        logger.info { "Создание расписания ${timetable.workingDate} ${timetable.store.name}" }
        return timetableRepository.save(timetable)
    }

    override fun findMaxWorkingDateByStoreId(storeId: Long): Timetable? =
            timetableRepository.findMaxWorkingDateByStoreId(storeId)

    override fun findAll(): Collection<Timetable> =
            timetableRepository.findAll()

    private fun generateTimetableByConfig(config: TimetableConfig, day: LocalDate, storeId: Long) = Timetable(
            startTime = config.startTime,
            endTime = config.endTime,
            workingDay = true,
            workingDate = day,
            store = storeService.findById(storeId))
}
