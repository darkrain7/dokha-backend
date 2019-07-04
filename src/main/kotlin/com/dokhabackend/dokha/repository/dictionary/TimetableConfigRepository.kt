package com.dokhabackend.dokha.repository.dictionary

import com.dokhabackend.dokha.entity.dictionary.DayOfWeek
import com.dokhabackend.dokha.entity.dictionary.TimetableConfig
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*


/**
 * Semenov A.E.
 * Created 18.06.2019
 *
 **/

@Repository
interface TimetableConfigRepository : CrudRepository<TimetableConfig, Long> {

    fun findByDayOfWeekAndStoreId(dawOfWeek: DayOfWeek, storeId: Long) : Optional<TimetableConfig>
}