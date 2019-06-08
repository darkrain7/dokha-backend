package com.dokhabackend.dokha.rest

import com.dokhabackend.dokha.core.RestResponse
import com.dokhabackend.dokha.dto.AuthenticationRequest
import com.dokhabackend.dokha.entity.User
import com.dokhabackend.dokha.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@CrossOrigin
@RequestMapping(consumes = [MediaType.APPLICATION_JSON_UTF8_VALUE], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
class AuthenticationController
@Autowired constructor(private val userService: UserService) {

    @RequestMapping(value = ["/login"], method = [RequestMethod.POST])
    fun login(@RequestBody authenticationRequest: AuthenticationRequest): RestResponse<String> {

        val token = userService.login(authenticationRequest.login, authenticationRequest.password)

        return RestResponse(token)
    }

    @RequestMapping(value = ["/register"], method = [RequestMethod.POST])
    fun register(@RequestBody authenticationRequest: AuthenticationRequest): RestResponse<User> {

        val registeredUser = userService.register(authenticationRequest.login, authenticationRequest.password)

        return RestResponse(registeredUser)
    }
}
