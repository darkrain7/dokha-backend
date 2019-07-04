package com.dokhabackend.dokha.service.dictionary

import com.dokhabackend.dokha.entity.dictionary.DayOfWeek
import com.dokhabackend.dokha.entity.dictionary.TimetableConfig

/**
 * Created by SemenovAE on 04.07.2019

 */

interface TimetableConfigService {

    fun findByDayOfWeekAndStoreId(dawOfWeek: DayOfWeek, storeId: Long): TimetableConfig
}