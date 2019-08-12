package com.dokhabackend.dokha.service.scheduling

import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.security.UserRoleEnum
import com.dokhabackend.dokha.service.TimetableService
import com.dokhabackend.dokha.service.dictionary.StoreService
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import javax.transaction.Transactional
import kotlin.collections.ArrayList

/**
 * Created by SemenovAE on 28.06.2019

 */

private val logger = KotlinLogging.logger {}

@Component
class TimetableFillScheduler
@Autowired constructor(private val timetableService: TimetableService,
                       private val storeService: StoreService) {

    private val oneDayStep = 1L
    private val schedulerName: String = "TimetableFiller"

//    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(fixedDelayString = "10000")
    @Transactional
    fun fillTimetable() {

        logger.info { "Запускаю обновление расписания" }

        authTimetableFiller()

        val currentDate = LocalDate.now()

        val nextSevenDays = getNexSevenDaysCalendar()

        val latestTimetableByStoreId = storeService.findAll()
                .associateBy({ it.id }, { timetableService.findMaxWorkingDateByStoreId(it.id) })
                .map { getTimetableOrDefault(it, currentDate) }

        val createdTimetables: MutableList<Timetable> = ArrayList()

        latestTimetableByStoreId.map {
            for (currentDayValue in it.workingDate.toEpochDay()..nextSevenDays.toEpochDay() step oneDayStep) {

                val timetable = timetableService.generateDefaultTimetable(LocalDate.ofEpochDay(currentDayValue), it.store.id)

                val create = timetableService.create(timetable)
                createdTimetables.add(create)
            }
        }

        logger.info { "Создано новых: ${createdTimetables.size}" }
        createdTimetables.forEach { logger.info { it } }
    }

    private fun authTimetableFiller() {
        val authorities = ArrayList<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(UserRoleEnum.ADMIN.name))
        val auth = UsernamePasswordAuthenticationToken(schedulerName, schedulerName, authorities)
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun getTimetableOrDefault(it: Map.Entry<Long, Timetable?>, currentDate: LocalDate): Timetable {
        return if (it.value == null)
            timetableService.generateDefaultTimetable(currentDate, it.key)
        else
            it.value!!
    }

    private fun getNexSevenDaysCalendar(): LocalDate {
        val nextSevenDays = Calendar.getInstance()
        nextSevenDays.set(Calendar.HOUR_OF_DAY, 0)
        nextSevenDays.set(Calendar.MINUTE, 0)
        nextSevenDays.set(Calendar.SECOND, 0)
        nextSevenDays.add(Calendar.DAY_OF_MONTH, 7)
        return java.sql.Date(nextSevenDays.timeInMillis).toLocalDate()

    }
}