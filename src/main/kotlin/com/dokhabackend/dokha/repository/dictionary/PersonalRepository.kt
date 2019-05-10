package com.dokhabackend.dokha.repository.dictionary

import com.dokhabackend.dokha.entity.dictionary.Personal
import org.springframework.data.repository.CrudRepository


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

interface PersonalRepository : CrudRepository<Personal, Long> {

}