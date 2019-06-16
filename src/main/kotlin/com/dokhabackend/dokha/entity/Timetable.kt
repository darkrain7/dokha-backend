package com.dokhabackend.dokha.entity

import com.dokhabackend.dokha.entity.dictionary.TimetableConfig
import javax.persistence.*


/**
 * Semenov A.E.
 * Created 16.06.2019
 *
 **/

@Entity
@Table(name = "timetable")
data class Timetable(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,

        @Column(name = "isWorkingDay")
        val workingDay: Boolean,

        @Column(name = "date")
        val date: Long,

        @ManyToOne
        @JoinColumn(name = "timetable_config_id")
        val timetableConfig: TimetableConfig
)