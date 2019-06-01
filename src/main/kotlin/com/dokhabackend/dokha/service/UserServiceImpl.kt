package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.entity.User
import com.dokhabackend.dokha.repository.UserRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import java.util.*


@Slf4j
@Service
class UserServiceImpl
@Autowired constructor(private val userRepository: UserRepository) : UserService, UserDetailsService {

    override fun loadUserByUsername(login: String): UserDetails {

        val user = findByLogin(login)
        // указываем роли для этого пользователя
        val roles = HashSet<GrantedAuthority>()
        user.roles.forEach { r -> roles.add(SimpleGrantedAuthority(r.name)) }

        // на основании полученных данных формируем объект UserDetails
        // который позволит проверить введенный пользователем логин и пароль
        // и уже потом аутентифицировать пользователя
        return org.springframework.security.core.userdetails.User(
                user.login,
                user.password,
                roles)
    }

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

        SecurityContextHolder.getContext().authentication.details

        SecurityContextHolder.getContext().authentication.credentials

        val user = userRepository.findByLoginAndPassword(login, password)

        return user.orElseThrow { throw IllegalAccessError("User Not Found") }

    }

    private fun checkUserLoginExist(user: User) {
        val checkUserLogin = userRepository.findByLogin(user.login).isPresent
        if (checkUserLogin)
            throw IllegalArgumentException("Login already exist")
    }
}