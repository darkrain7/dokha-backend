package com.dokhabackend.dokha.service

import com.dokhabackend.dokha.entity.Timetable
import com.dokhabackend.dokha.repository.TimetableRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TimetableServiceImpl
@Autowired constructor(val timetableRepository: TimetableRepository) : TimetableService {

    override fun findByStoreIdAndWorkingDate(storeId: Long, workingDate: Long): Timetable {
        val timetable = timetableRepository.findByStoreIdAndWorkingDate(storeId, workingDate)

        return timetable.orElseThrow { throw IllegalStateException("not found") }
    }
}