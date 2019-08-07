package com.dokhabackend.dokha.service.scheduling

import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.security.UserRoleEnum
import com.dokhabackend.dokha.service.TimetableService
import com.dokhabackend.dokha.service.dictionary.StoreService
import com.dokhabackend.dokha.util.Util
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*

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

    //    @Scheduled(cron = "0 0 3 * * *")
    fun fillTimetable() {

        val authorities = ArrayList<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(UserRoleEnum.ADMIN.name))
        val auth = UsernamePasswordAuthenticationToken(schedulerName, schedulerName, authorities)
        SecurityContextHolder.getContext().authentication = auth

        logger.info { "Ура у меня есть логер" }

        val currentCalendar = Calendar.getInstance()
        currentCalendar.time = Date()

        val currentTruncDate = Util().truncDate(currentCalendar.timeInMillis)

        val nextSevenDays = getNexSevenDaysCalendar()

        val latestTimetableByStoreId = storeService.findAll()
                .associateBy({ it.id }, { timetableService.findMaxWorkingDateByStoreId(it.id) })
                .map { getTimetableOrDefault(it, currentTruncDate) }

        latestTimetableByStoreId.map {
            for (currentDayValue in it.workingDate.toEpochDay()..nextSevenDays.toEpochDay() step oneDayStep) {

                val timetable = buildTimetable(it, LocalDate.ofEpochDay(currentDayValue))

                timetableService.create(timetable)
            }
        }
    }

    private fun buildTimetable(it: Timetable, currentDayValue: LocalDate): Timetable =
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

    private fun getNexSevenDaysCalendar(): LocalDate {
        val nextSevenDays = Calendar.getInstance()
        nextSevenDays.set(Calendar.HOUR_OF_DAY, 0)
        nextSevenDays.set(Calendar.MINUTE, 0)
        nextSevenDays.set(Calendar.SECOND, 0)
        nextSevenDays.add(Calendar.DAY_OF_MONTH, 7)
        return LocalDate.from(nextSevenDays.toInstant())

    }
}