package com.dokhabackend.dokha.config.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

/**
 * @author v.butuzov
 * Конфигурация сисурити
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig
@Autowired constructor(private val unauthorizedHandler: JwtAuthenticationEntryPoint) : WebSecurityConfigurerAdapter() {

    @Qualifier("userServiceImpl")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    fun globalUserDetails(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService>(userDetailsService)
                .passwordEncoder(encoder())
    }

    @Bean
    fun authenticationTokenFilterBean(): JwtAuthenticationFilter = JwtAuthenticationFilter()

    override fun configure(http: HttpSecurity) {
        http
                .cors().and().csrf().disable()
                .authorizeRequests().antMatchers("/*").permitAll()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)

        http.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter::class.java)

    }

    @Bean
    fun encoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}
