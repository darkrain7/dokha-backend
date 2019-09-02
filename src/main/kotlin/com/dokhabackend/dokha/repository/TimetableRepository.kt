package com.dokhabackend.dokha.repository

import com.dokhabackend.dokha.entity.Timetable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.util.*


/**
 * Semenov A.E.
 * Created 18.06.2019
 *
 **/

@Repository
interface TimetableRepository : CrudRepository<Timetable, Long> {

    override fun findAll(): Collection<Timetable>

    fun findByStoreIdAndWorkingDate(storeId: Long, workingDate: LocalDate): Optional<Timetable>

    fun findByWorkingDateAfter(workingDate: Long): Collection<Timetable>

    @Query("SELECT * " +
            " FROM dokha.timetable t" +
            " WHERE t.working_date = (SELECT max(t2.working_date) FROM dokha.timetable t2 WHERE t2.store_id = :storeId)" +
            " AND store_id = :storeId", nativeQuery = true)
    fun findMaxWorkingDateByStoreId(storeId: Long): Timetable?
}