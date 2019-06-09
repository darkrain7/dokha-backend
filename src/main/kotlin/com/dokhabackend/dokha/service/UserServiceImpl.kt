package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.config.security.JwtTokenUtil
import com.dokhabackend.dokha.core.logger
import com.dokhabackend.dokha.entity.User
import com.dokhabackend.dokha.repository.UserRepository
import com.dokhabackend.dokha.security.UserRoleEnum
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class UserServiceImpl
@Autowired constructor(private val userRepository: UserRepository,
                       private val jwtTokenUtil: JwtTokenUtil,
                       private val passwordEncoder: BCryptPasswordEncoder,
                       private val authenticationManager: AuthenticationManager) : UserService, UserDetailsService {

    val logger = logger()

    override fun login(login: String, password: String): String {

        val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(login, password))

        SecurityContextHolder.getContext().authentication = authentication

        val user = findByLogin(login)

        logger.info("Авторизация пользователя $login")
        return jwtTokenUtil.generateToken(user)
    }

    override fun register(login: String, password: String): User {

        logger.info("Регистрация нового пользователя $login")

        val user = User(
                id = null,
                login = login,
                password = passwordEncode(password),
                roles = setOf(UserRoleEnum.ADMIN))

        return createUser(user)
    }

    override fun loadUserByUsername(login: String): UserDetails {

        val user = findByLogin(login)

        val roles = HashSet<GrantedAuthority>()
        user.roles.forEach { r -> roles.add(SimpleGrantedAuthority(r.name)) }

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

    private fun checkUserLoginExist(user: User) {
        val checkUserLogin = userRepository.findByLogin(user.login).isPresent
        if (checkUserLogin)
            throw IllegalArgumentException("Login already exist")
    }

    private fun passwordEncode(password: String) = passwordEncoder.encode(password)
}
