package com.dokhabackend.dokha.security

/**
 * Semenov A.E.
 * Created 01.06.2019
 */

enum class UserRoleEnum(val id: Long) {

    ADMIN(0),
    USER(1),
    ANONYMOUS(2)
}
