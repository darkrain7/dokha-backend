package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.dto.AuthenticationRequest
import com.dokhabackend.dokha.entity.User
import com.dokhabackend.dokha.service.UserService
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@Controller
@CrossOrigin
class AuthenticationController
@Autowired constructor(private val userService: UserService) {

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<String> {

        val token = userService.login(authenticationRequest.login, authenticationRequest.password)

        return ResponseEntity.ok(token)
    }

    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    fun register(@RequestBody authenticationRequest: AuthenticationRequest): ResponseEntity<User> {

        val registeredUser = userService.register(authenticationRequest.login, authenticationRequest.password)

        return ResponseEntity.ok(registeredUser)
    }
}
