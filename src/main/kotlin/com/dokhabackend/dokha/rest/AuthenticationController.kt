package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.config.security.JwtTokenUtil
import com.dokhabackend.dokha.dto.AuthenticationRequest
import com.dokhabackend.dokha.entity.User
import com.dokhabackend.dokha.entity.constant.RoleEnum
import com.dokhabackend.dokha.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@CrossOrigin
class AuthenticationController
@Autowired constructor(private val passwordEncoder: BCryptPasswordEncoder,
                       private val authenticationManager: AuthenticationManager,
                       private val jwtTokenUtil: JwtTokenUtil,
                       private val userService: UserService) {

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<String> {

        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        authenticationRequest.login,
                        authenticationRequest.password))

        SecurityContextHolder.getContext().authentication = authentication
        val user = userService.findByLogin(authenticationRequest.login)

        return ResponseEntity.ok(jwtTokenUtil.generateToken(user))
    }

    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    fun register(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<User> {

        val user = User(null,
                authenticationRequest.login,
                passwordEncoder.encode(authenticationRequest.password),
                setOf(RoleEnum.ADMIN))

        val createUser = userService.createUser(user)

        return ResponseEntity.ok<User>(createUser)
    }
}
