package com.dokhabackend.dokha.repository

import com.dokhabackend.dokha.entity.Timetable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


/**
 * Semenov A.E.
 * Created 18.06.2019
 *
 **/

@Repository
interface TimetableRepository : CrudRepository<Timetable, Long> {

    fun findByStoreIdAndWorkingDate(storeId: Long, workingDate: Long): Optional<Timetable>

    fun findByWorkingDateAfter(workingDate: Long): Collection<Timetable>

    @Query("SELECT *" +
            " FROM timetable t " +
            " WHERE t.working_date = " +
            " (SELECT max(t2.working_date) FROM timetable t2 WHERE t2.store_id = :storeId)", nativeQuery = true)
    fun findMaxWorkingDateByStoreId(storeId: Long): Timetable?
}