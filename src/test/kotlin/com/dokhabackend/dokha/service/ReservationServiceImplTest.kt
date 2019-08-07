package com.dokhabackend.dokha.service

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
 * Semenov A.E.
 * Created 22.06.2019
 */

@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
@ActiveProfiles(profiles = ["test"])
@WithMockUser(authorities = ["ADMIN"])
class ReservationServiceImplTest {

    @Autowired
    private lateinit var reservationService: ReservationService

    @Test
    @Sql(value = ["/GeneralTestData"])
    fun findFreeReservation() {

        val currentTime = 1561187030000L

        val findFreeReservation = reservationService.findFreeReservation(-1, currentTime, 2)

    }
}