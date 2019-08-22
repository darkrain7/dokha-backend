package com.dokhabackend.dokha.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
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

    @Autowired
    private lateinit var jwtProperties: JwtProperties

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val header = req.getHeader(jwtProperties.header)
        val uri = req.requestURI

        if (uri.contains("register") || uri.contains("login")) {

        } else {

            if (header == null || header.isEmpty())
                throw AuthenticationException("Вася а где токен?")

            val login = JwtTokenUtil(jwtProperties).getUsernameFromToken(header)

            val userDetails = SecurityContextHolder.getContext().authentication.principal as UserDetails

            if (userDetails.username != login)
                throw AuthenticationException("Пользователь не авторизован")
        }

        chain.doFilter(req, res)
    }
}