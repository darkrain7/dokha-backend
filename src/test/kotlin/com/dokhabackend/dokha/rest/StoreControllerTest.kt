package com.dokhabackend.dokha.rest

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext

/**
 * Semenov A.E.
 * Created 10.05.2019
 */

@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
@WithMockUser(authorities = ["ADMIN"])
class StoreControllerTest {

    @Autowired
    lateinit var context: WebApplicationContext
    lateinit var mvc: MockMvc

    @Before
    fun setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(this.context)
                .build()
    }

    @Test
    @Sql(value = ["/store/storeTestData"])
    fun getById() {

        mvc.perform(get("/store?id={id}", -1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.id", `is`(-1)))
                .andExpect(jsonPath("$.data.name", `is`("testName")))
                .andExpect(jsonPath("$.data.location", `is`("testLocation")))
    }

    @Test
    @Sql(value = ["/store/storeTestData"])
    fun getAll() {

        mvc.perform(get("/store/findAll")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data", hasSize<Long>(2)))
    }
}