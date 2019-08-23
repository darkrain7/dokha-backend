package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.config.security.JwtTokenUtil
import com.dokhabackend.dokha.dto.AuthenticationResponse
import com.dokhabackend.dokha.entity.User
import com.dokhabackend.dokha.repository.UserRepository
import com.dokhabackend.dokha.security.UserRoleEnum
import mu.KotlinLogging
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

private val logger = KotlinLogging.logger {}

@Service
class UserServiceImpl
@Autowired constructor(private val userRepository: UserRepository,
                       private val jwtTokenUtil: JwtTokenUtil,
                       private val passwordEncoder: BCryptPasswordEncoder,
                       private val authenticationManager: AuthenticationManager)
    : UserService, UserDetailsService {


    override fun login(login: String, password: String): AuthenticationResponse {
        try {
            val authentication = authenticationManager.authenticate(UsernamePasswordAuthenticationToken(login, password))
            SecurityContextHolder.getContext().authentication = authentication

        } catch (e: Exception) {
            throw IllegalAccessError("Неверный логин\"пароль")
        }

        val user = findByLogin(login)
        logger.info("Авторизация пользователя $login")

        return AuthenticationResponse(
                jwtTokenUtil.generateToken(user),
                user.roles.map { it.id }.toSet())
    }

    override fun registerAndLogin(login: String, password: String): AuthenticationResponse {

        logger.info("Регистрация нового пользователя $login")

        val user = User(
                login = login,
                password = passwordEncode(password),
                roles = setOf(UserRoleEnum.USER))

        createUser(user)

        return login(login, password)
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

    override fun findById(id: Long): User =
            userRepository.findById(id).orElseThrow { throw IllegalArgumentException("Пользователь не найден") }

    override fun findAll(): Collection<User> = userRepository.findAll()

    override fun findByLoginAndPassword(login: String, password: String): User =
            userRepository.findByLoginAndPassword(login, password)
                    .orElseThrow { throw IllegalArgumentException("Пользователь не найден") }

    override fun findByLogin(login: String): User =
            userRepository.findByLogin(login)
                    .orElseThrow { throw IllegalArgumentException("Пользователь не найден") }

    override fun createUser(user: User): User {
        checkUserLoginExist(user)
        return userRepository.save(user)
    }

    private fun checkUserLoginExist(user: User) {
        val checkUserLogin = userRepository.findByLogin(user.login).isPresent
        if (checkUserLogin)
            throw IllegalArgumentException("Такой логин уже существует")
    }

    private fun passwordEncode(password: String) = passwordEncoder.encode(password)
}
