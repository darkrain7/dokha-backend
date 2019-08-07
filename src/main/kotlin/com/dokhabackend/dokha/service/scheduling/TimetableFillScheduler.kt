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

/**
 * Created by SemenovAE on 28.06.2019

 */

private val logger = KotlinLogging.logger {}

@Component
class TimetableFillScheduler
@Autowired constructor(private val timetableService: TimetableService,
                       private val storeService: StoreService) {

    //    private val oneDayStep = 1L * 24L * 60L * 60L * 1000L
    private val oneDayStep = 1L
    private val schedulerName: String = "TimetableFiller"

//    @Scheduled(cron = "10,20,30,40,50 * * * * *")
    @Transactional
    fun fillTimetable() {

        val authorities = ArrayList<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(UserRoleEnum.ADMIN.name))
        val auth = UsernamePasswordAuthenticationToken(schedulerName, schedulerName, authorities)
        SecurityContextHolder.getContext().authentication = auth

        logger.info { "Ура у меня есть логер" }

        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = Date()

        val currentDate = LocalDate.now()

        val nextSevenDays = getNexSevenDaysCalendar()

        val latestTimetableByStoreId = storeService.findAll()
                .associateBy({ it.id }, { timetableService.findMaxWorkingDateByStoreId(it.id) })
                .map { getTimetableOrDefault(it, currentDate) }

        latestTimetableByStoreId.map {
            for (currentDayValue in it.workingDate.toEpochDay()..nextSevenDays.toEpochDay() step oneDayStep) {

                val timetable = timetableService.generateDefaultTimetable(currentDate, it.store.id)

                timetableService.create(timetable)
            }
        }
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