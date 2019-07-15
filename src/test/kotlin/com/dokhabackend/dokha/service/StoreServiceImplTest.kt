package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.dto.dictionary.StoreDto
import com.dokhabackend.dokha.service.dictionary.StoreService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
 * Created 03.05.2019
 */
@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
@ActiveProfiles(profiles = ["test"])
@WithMockUser(authorities = ["ADMIN"])
class StoreServiceImplTest {

    @Autowired
    private lateinit var storeService: StoreService

    @Test
    fun create() {

        val store = StoreDto(
                name = "testName",
                location = "testLocation",
                photo = ByteArray(0))

        val (id, name, location) = storeService.create(store)

        assertNotNull(id)
        assertEquals("testName", name)
        assertEquals("testLocation", location)
    }

    @Test
    @Sql(value = ["/store/storeTestData"])
    fun findById() {
        val store = storeService.findById(-1)

        assertEquals(-1L, store.id)
        assertEquals("testName", store.name)
        assertEquals("testLocation", store.location)
    }

    @Test
    @Sql(value = ["/store/storeTestData"])
    fun findAll() {

        val stores = storeService.findAll()

        assertEquals(2, stores.count())
    }
}