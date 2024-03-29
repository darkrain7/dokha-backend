package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.dto.AuthenticationResponse
import com.dokhabackend.dokha.entity.User


/**
 * Semenov A.E.
 * Created 19.05.2019
 *
 **/
interface UserService {

    fun login(login: String, password: String): AuthenticationResponse

    fun registerAndLogin(login: String, password: String) : AuthenticationResponse

    fun findById(id: Long): User

    fun findAll(): Collection<User>

    fun findByLoginAndPassword(login: String, password: String): User

    fun findByLogin(login: String): User

    fun createUser(user: User): User
}