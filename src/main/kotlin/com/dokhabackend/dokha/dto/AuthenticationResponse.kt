package com.dokhabackend.dokha.dto

import com.dokhabackend.dokha.security.UserRoleEnum

/**
 * Semenov A.E.
 * Created 19.05.2019
 */
data class AuthenticationResponse(

        val userToken: String,

        val userRoles: Set<Long>
)
