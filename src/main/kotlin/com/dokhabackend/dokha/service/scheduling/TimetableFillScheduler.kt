package com.dokhabackend.dokha.service.scheduling

import com.dokhabackend.dokha.Util.Util
import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.service.TimetableService
import com.dokhabackend.dokha.service.dictionary.StoreService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by SemenovAE on 28.06.2019

 */

private val logger = KotlinLogging.logger {}

@Component
class TimetableFillScheduler
@Autowired constructor(private val timetableService: TimetableService,
                       private val storeService: StoreService) {

    private val oneDayStep = 1L * 24L * 60L * 60L * 1000L

    fun fillTimetable() {

        logger.info { "Ура у меня есть логер" }

        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = Date()

        val currentTruncDate = Util().truncDate(currentCalendar.timeInMillis)

        val nextSevenDays = getNexSevenDaysCalendar()

        val timetableByStoreId = storeService.findAll()
                .associateBy({ it.id }, { timetableService.findMaxWorkingDateByStoreId(it.id) })
                .map { getTimetableOrDefault(it, currentTruncDate) }


        timetableByStoreId.map {
            for (currentDayValue in (it.workingDate + oneDayStep)..nextSevenDays.timeInMillis step oneDayStep) {

                val timetable = buildTimetable(it, currentDayValue)

                timetableService.create(timetable)
            }
        }
    }

    private fun buildTimetable(it: Timetable, currentDayValue: Long): Timetable =
            Timetable(
                    startTime = it.startTime,
                    endTime = it.endTime,
                    workingDate = currentDayValue,
                    workingDay = it.workingDay,
                    store = it.store)

    private fun getTimetableOrDefault(it: Map.Entry<Long, Timetable?>, currentTruncDate: Long): Timetable {
        return if (it.value == null)
            timetableService.generateDefaultTimetable(currentTruncDate, it.key)
        else
            it.value!!
    }

    private fun getNexSevenDaysCalendar(): Calendar {
        val nextSevenDays = Calendar.getInstance()
        nextSevenDays.set(Calendar.HOUR_OF_DAY, 0)
        nextSevenDays.set(Calendar.MINUTE, 0)
        nextSevenDays.set(Calendar.SECOND, 0)
        nextSevenDays.add(Calendar.DAY_OF_MONTH, 7)
        return nextSevenDays
    }
}