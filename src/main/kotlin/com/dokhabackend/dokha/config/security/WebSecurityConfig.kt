package com.dokhabackend.dokha.config.security

import com.dokhabackend.dokha.config.BaseExceptionHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
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
@Autowired constructor(private val exceptionHandler: BaseExceptionHandler) : WebSecurityConfigurerAdapter() {

    @Qualifier("userServiceImpl")
    @Autowired
    private lateinit var userDetailsService: UserDetailsService

    @Autowired
    fun globalUserDetails(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService<UserDetailsService>(userDetailsService)
                .passwordEncoder(encoder())
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager = super.authenticationManagerBean()

    @Bean
    fun authenticationTokenFilterBean(): JwtAuthenticationFilter = JwtAuthenticationFilter()

    override fun configure(http: HttpSecurity) {
        http
                .cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/login", "/register").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(exceptionHandler)
                .and()
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun encoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()
}
