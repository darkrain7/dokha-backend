package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.entity.Role
import com.dokhabackend.dokha.repository.RoleRepository
import lombok.extern.slf4j.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Slf4j
@Service
class RoleServiceImpl
@Autowired constructor(private val roleRepository: RoleRepository) : RoleService {

    override fun findById(id: Int): Role {

        val role = roleRepository.findById(id)

        return role.orElseThrow { throw Exception("RoleEnum Not Found") }
    }

    override fun findAll(): Collection<Role> = roleRepository.findAll()

}