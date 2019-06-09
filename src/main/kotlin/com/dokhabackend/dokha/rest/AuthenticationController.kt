package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.core.RestResponse
import com.dokhabackend.dokha.dto.AuthenticationRequest
import com.dokhabackend.dokha.entity.User
import com.dokhabackend.dokha.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin
class AuthenticationController
@Autowired constructor(private val userService: UserService) {

    @PostMapping(value = ["/login"])
    fun login(@RequestBody authenticationRequest: AuthenticationRequest): RestResponse<String> {

        val token = userService.login(authenticationRequest.login, authenticationRequest.password)

        return RestResponse(token)
    }

    @PostMapping(value = ["/register"])
    fun register(@RequestBody authenticationRequest: AuthenticationRequest): RestResponse<User> {

        val registeredUser = userService.register(authenticationRequest.login, authenticationRequest.password)

        return RestResponse(registeredUser)
    }
}
