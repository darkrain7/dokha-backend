package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.entity.User
import com.dokhabackend.dokha.repository.UserRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class UserServiceImpl
@Autowired constructor(private val userRepository: UserRepository) : UserService {

    override fun findById(id: Long): User {
        val user = userRepository.findById(id)

        return user.orElseThrow { throw Exception("User Not Found") }
    }

    override fun findAll(): Collection<User> = userRepository.findAll()

    override fun findByLoginAndPassword(login: String, password: String): User {
        val user = userRepository.findByLoginAndPassword(login, password)

        return user.orElseThrow { throw Exception("User Not Found") }
    }

    override fun findByLogin(login: String): User {
        val user = userRepository.findByLogin(login)

        return user.orElseThrow { throw Exception("User Not Found") }
    }

    override fun createUser(user: User): User {

        checkUserLoginExist(user)

        return userRepository.save(user)
    }

    override fun login(login: String, password: String): User {

        val user = userRepository.findByLoginAndPassword(login, password)

        return user.orElseThrow { throw IllegalAccessError("User Not Found") }

    }

    private fun checkUserLoginExist(user: User) {
        val checkUserLogin = userRepository.findByLogin(user.login).isPresent
        if (checkUserLogin)
            throw IllegalArgumentException("Login already exist")
    }
}