package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.entity.Role
import com.dokhabackend.dokha.entity.constant.RoleEnum
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional

/**
 * Semenov A.E.
 * Created 19.05.2019
 */

@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
class RoleServiceImplTest {

    @Autowired
    lateinit var roleService: RoleService

    @Test
    fun findById() {

        val role = roleService.findById(RoleEnum.ADMIN.getId())

        val assertRole = Role(id = 1, name = "Администратор", alias = "ADMIN")

        assertEquals(assertRole, role)
    }

    @Test(expected = Exception::class)
    fun findByIdNotFound() {

        roleService.findById(-1)
    }

    @Test
    fun findAll() {

        val roles = roleService.findAll()

        assertEquals(2, roles.size)
    }
}