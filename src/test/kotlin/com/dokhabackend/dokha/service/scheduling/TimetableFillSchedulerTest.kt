package com.dokhabackend.dokha.service.scheduling

import com.dokhabackend.dokha.service.TimetableService
import mu.KotlinLogging
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

/**
 * Created by SemenovAE on 10.07.2019
 */

private val logger = KotlinLogging.logger {}

@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
@ActiveProfiles(profiles = ["test"])
@WithMockUser(authorities = ["ADMIN"])
@Sql(value = ["/GeneralTestData"])
class TimetableFillSchedulerTest {

    @Autowired
    private lateinit var scheduler: TimetableFillScheduler

    @Autowired
    private lateinit var timetableService: TimetableService

    @Test
    fun fillTimetable() {

        scheduler.fillTimetable()

        val findAll = timetableService.findAll()

        logger.info { findAll.size }

    }
}