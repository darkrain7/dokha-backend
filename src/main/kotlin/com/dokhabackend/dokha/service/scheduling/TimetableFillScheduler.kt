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
import javax.transaction.Transactional

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

    @Scheduled(cron = "30 * * * * *")
    @Transactional
    fun fillTimetable() {

        logger.info { "Запускаю обновление расписания" }

        authTimetableFiller()

        val currentDate = LocalDate.now()

        val nextSevenDays = currentDate.plusDays(7)

        val latestTimetableByStoreId = storeService.findAll()
                .associateBy({ it.id }, { timetableService.findMaxWorkingDateByStoreId(it.id) })
                .map { getTimetableOrDefault(it, currentDate) }

        val createdTimetables: MutableList<Timetable> = ArrayList()

        latestTimetableByStoreId
                .map {
                    for (currentDayValue in it.workingDate.toEpochDay()..nextSevenDays.toEpochDay() step oneDayStep) {

                        val timetableAlreadyExist = timetableService.isExistByStoreIdAndWorkingDate(it.store.id, LocalDate.ofEpochDay(currentDayValue))
                        if (timetableAlreadyExist) continue

                        val timetable = timetableService.generateDefaultTimetable(LocalDate.ofEpochDay(currentDayValue), it.store.id)

                        val create = timetableService.create(timetable)
                        createdTimetables.add(create)
                    }
                }

        logger.info { "Создано новых: ${createdTimetables.size}" }
    }

    private fun authTimetableFiller() {
        val authorities = ArrayList<GrantedAuthority>()
        authorities.add(SimpleGrantedAuthority(UserRoleEnum.ADMIN.name))
        val auth = UsernamePasswordAuthenticationToken(schedulerName, schedulerName, authorities)
        SecurityContextHolder.getContext().authentication = auth
    }

    private fun getTimetableOrDefault(it: Map.Entry<Long, Timetable?>, currentDate: LocalDate): Timetable {
        val value = it.value
        return value ?: timetableService.generateDefaultTimetable(currentDate, it.key)
    }
}