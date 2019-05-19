package com.dokhabackend.dokha.repository

import com.dokhabackend.dokha.entity.Role
import org.springframework.data.repository.CrudRepository


/**
 * Semenov A.E.
 * Created 19.05.2019
 *
 **/
interface RoleRepository : CrudRepository<Role, Int> {

    override fun findAll(): Collection<Role>
}