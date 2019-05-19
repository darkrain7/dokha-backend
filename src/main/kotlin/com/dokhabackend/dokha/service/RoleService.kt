package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.entity.Role

/**
 * Semenov A.E.
 * Created 19.05.2019
 *
 **/
interface RoleService {

    fun findById(id: Int) : Role

    fun findAll() : Collection<Role>
}