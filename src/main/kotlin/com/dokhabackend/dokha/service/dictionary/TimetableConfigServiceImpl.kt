package com.dokhabackend.dokha.service.dictionary

import com.dokhabackend.dokha.entity.dictionary.TimetableConfig
import com.dokhabackend.dokha.repository.dictionary.TimetableConfigRepository
import org.springframework.stereotype.Service

@Service
class TimetableConfigServiceImpl
constructor(val timetableConfigRepository: TimetableConfigRepository) : TimetableConfigService {

    override fun findByDayOfWeekAndStoreId(dawOfWeekId: Long, storeId: Long): TimetableConfig =
        timetableConfigRepository.findByDayOfWeekIdAndStoreId(dawOfWeekId, storeId).orElseThrow { IllegalStateException("Not found") }
}