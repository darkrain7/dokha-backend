package com.dokhabackend.dokha

import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest
@ActiveProfiles(profiles = ["test"])
class DokhaApplicationTests {

    @Test
    fun contextLoads() {
    }

}
