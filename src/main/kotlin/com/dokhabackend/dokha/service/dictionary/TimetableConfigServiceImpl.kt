package com.dokhabackend.dokha.service.dictionary

import com.dokhabackend.dokha.entity.dictionary.DayOfWeek
import com.dokhabackend.dokha.repository.dictionary.TimetableConfigRepository
import org.springframework.stereotype.Service

@Service
class TimetableConfigServiceImpl
constructor(val timetableConfigRepository: TimetableConfigRepository) : TimetableConfigService {

    override fun findByDayOfWeekAndStoreId(dawOfWeek: DayOfWeek, storeId: Long) =
        timetableConfigRepository.findByDayOfWeekAndStoreId(dawOfWeek, storeId).orElseThrow { IllegalStateException("Not found") }
}