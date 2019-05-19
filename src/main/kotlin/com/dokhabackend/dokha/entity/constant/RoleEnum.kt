package com.dokhabackend.dokha.entity.constant

import java.util.*

/**
 * Роли
 */

enum class RoleEnum(private val id: Int?) {

    ADMIN(1),
    USER(2);

    fun getId(): Int {
        return id!!
    }

    companion object {

        private val ROLES = HashMap<Int?, RoleEnum?>()

        init {
            for (value in values()) {
                ROLES[value.id] = value
            }
        }

        fun getById(id: Int?): RoleEnum? {
            return ROLES[id]
        }
    }
}
