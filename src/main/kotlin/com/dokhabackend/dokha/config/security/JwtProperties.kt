package com.dokhabackend.dokha.config.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Created by SemenovAE on 22.08.2019

 */

@Component
@ConfigurationProperties(prefix = "jwt")
class JwtProperties {

    lateinit var header: String

    lateinit var signingKey: String

    var accessTokenValidityMillisecond: Long = 0
}