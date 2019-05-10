package com.dokhabackend.dokha.repository

import com.dokhabackend.dokha.entity.User
import org.springframework.data.repository.CrudRepository


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/
interface UserRepository : CrudRepository<User, Long> {
}