package com.dokhabackend.dokha.entity

import com.dokhabackend.dokha.entity.dictionary.TimetableConfig
import javax.persistence.*


/**
 * Semenov A.E.
 * Created 16.06.2019
 *
 **/

@Entity
@Table(name = "timetable", schema = "dokha")
data class Timetable(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @Column(name = "isWorkingDay")
        val workingDay: Boolean,

        @Column(name = "working_date")
        val workingDate: Long,

        @Column(name = "start_date")
        val startDate: Long,

        @Column(name = "end_date")
        val endDate: Long
)