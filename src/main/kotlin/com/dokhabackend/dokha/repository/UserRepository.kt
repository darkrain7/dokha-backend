package com.dokhabackend.dokha.repository

import com.dokhabackend.dokha.entity.User
import org.springframework.data.repository.CrudRepository
import java.util.*


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/
interface UserRepository : CrudRepository<User, Long> {

    override fun findAll(): Collection<User>

    fun findByLoginAndPassword(login: String, password: String): Optional<User>

    fun findByLogin(login: String): Optional<User>
}