package com.dokhabackend.dokha.entity

import com.dokhabackend.dokha.entity.dictionary.Store
import java.time.LocalDate
import java.time.LocalTime
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
        val id: Long = 0,

        @Basic
        @Column(name = "start_time")
        val startTime: LocalTime,

        @Basic
        @Column(name = "end_time")
        val endTime: LocalTime,

        @Basic
        @Column(name = "working_date")
        val workingDate: LocalDate,

        @Column(name = "working_day")
        val workingDay: Boolean,

        @ManyToOne
        @JoinColumn(name = "store_id")
        val store: Store
)