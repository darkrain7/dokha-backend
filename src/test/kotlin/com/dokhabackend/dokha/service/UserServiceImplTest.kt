package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.entity.User
import com.dokhabackend.dokha.entity.constant.RoleEnum
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
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
class UserServiceImplTest {

//    @Autowired
//    lateinit var userService: UserService
//
//    @Autowired
//    lateinit var roleService: RoleService
//
//    @Test
//    fun findById() {
//
//        val user = userService.findById(1)
//        val role = roleService.findById(RoleEnum.ADMIN.getId())
//
//        val assertUser = User(id = 1, login = "admin", password = "admin", roles = role)
//
//        assertEquals(assertUser, user)
//    }
//
//    @Test(expected = Exception::class)
//    fun findByIdNotFound() {
//
//        userService.findById(-1)
//    }
//
//
//    @Test
//    fun findAll() {
//
//        val users = userService.findAll()
//
//        assertEquals(1, users.size)
//    }
//
//    @Test
//    fun findByLoginAndPassword() {
//
//        val user = userService.findByLoginAndPassword("admin", "admin")
//
//        assertNotNull(user)
//    }
//
//    @Test
//    fun findByLogin() {
//
//        val user = userService.findByLogin("admin")
//
//        assertNotNull(user)
//    }
//
//    @Test
//    fun createUser() {
//
//        val role = roleService.findById(RoleEnum.USER.getId())
//        val user = User(id = null, login = "test", password = "test", roles = role)
//
//        val createdUser = userService.createUser(user)
//
//        assertEquals(user, createdUser)
//    }
//
//    @Test
//    fun login() {
//
//        val user = userService.login("admin", "admin")
//
//        assertNotNull(user)
//    }
}