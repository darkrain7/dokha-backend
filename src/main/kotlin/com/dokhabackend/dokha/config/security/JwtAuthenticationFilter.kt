package com.dokhabackend.dokha.config.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.security.sasl.AuthenticationException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author v.butuzov
 * Фильтр авторизации через jwt
 */

@Component
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Value("\${jwt.header}")
    private lateinit var HEADER_STRING: String

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val header = req.getHeader(HEADER_STRING)
        val uri = req.requestURI

        if (uri.contains("register") || uri.contains("login")) {

        } else {

            if (header == null || header.isEmpty()) {
                throw AuthenticationException("Вася а где токен?")
            }
        }

        chain.doFilter(req, res)
    }
}