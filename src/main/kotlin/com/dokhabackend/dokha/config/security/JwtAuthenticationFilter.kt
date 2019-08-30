package com.dokhabackend.dokha.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.security.authentication.RememberMeAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
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

    @Qualifier("userServiceImpl")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtProperties: JwtProperties

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val token = req.getHeader(jwtProperties.header)
        val uri = req.requestURI

        if (uri.contains("/register") || uri.contains("/login"))

        else if (token == null || token.isEmpty())
            throw AuthenticationException("Вася а где токен?")
        else {
            val login = JwtTokenUtil(jwtProperties).getUsernameFromToken(token)
            val user = userDetailsService.loadUserByUsername(login)

            SecurityContextHolder.getContext().authentication = RememberMeAuthenticationToken(token, user, user.authorities)
        }
        chain.doFilter(req, res)
    }
}