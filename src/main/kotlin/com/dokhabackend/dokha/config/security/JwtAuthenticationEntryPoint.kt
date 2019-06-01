package com.dokhabackend.dokha.config.security

import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.io.Serializable

/**
 * @author v.butuzov
 * EntryPoint авторизации через jwt
 */
@Component
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint, Serializable {

    override fun commence(request: HttpServletRequest,
                          response: HttpServletResponse,
                          authException: AuthenticationException) {

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован")
    }
}