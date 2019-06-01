package com.dokhabackend.dokha.config.security

import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.security.sasl.AuthenticationException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * @author v.butuzov
 * Фильтр авторизации через jwt
 */
@Slf4j
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Value("\${jwt.header}")
    private lateinit var HEADER_STRING: String

    @Qualifier("userServiceImpl")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    private lateinit var jwtTokenUtil: JwtTokenUtil

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val header = req.getHeader(HEADER_STRING)
        val uri = req.requestURI
        val login: String

        try {
            if (uri.contains("register") || uri.contains("login") || uri.contains("/v2/api-docs")) {

            } else {
                if (header == null || header.isEmpty()) {
                    throw AuthenticationException("Пользователь не авторизован")
                }

                login = jwtTokenUtil.getUsernameFromToken(header)

                if (login != null && SecurityContextHolder.getContext().authentication == null) {

                    val userDetails = userDetailsService.loadUserByUsername(login)

                    if (jwtTokenUtil.validateToken(header, userDetails)!!) {
                        val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                        authentication.details = WebAuthenticationDetailsSource().buildDetails(req)
                        logger.info("Вход выполнен. Пользователь: $login")
                        SecurityContextHolder.getContext().authentication = authentication
                    }
                }
            }
        } catch (e: Exception) {

            logger.error("Несуществующий логин", e)
            throw IllegalArgumentException("Пользователь не авторизован")
        }

        chain.doFilter(req, res)
    }
}