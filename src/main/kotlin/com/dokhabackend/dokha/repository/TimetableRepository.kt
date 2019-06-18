package com.dokhabackend.dokha.repository

import com.dokhabackend.dokha.entity.Timetable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository


/**
 * Semenov A.E.
 * Created 18.06.2019
 *
 **/

@Repository
interface TimetableRepository : CrudRepository<Timetable, Long> {
}