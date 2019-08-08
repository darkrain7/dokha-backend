package com.dokhabackend.dokha.entity.dictionary

import java.time.LocalTime
import javax.persistence.*


/**
 * Semenov A.E.
 * Created 16.06.2019
 *
 **/

@Entity
@Table(name = "s_timetable_config", schema = "dokha")
data class TimetableConfig(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(name = "start_time")
        val startTime: LocalTime,

        @Column(name = "end_time")
        val endTime: LocalTime,

        @ManyToOne
        @JoinColumn(name = "day_of_week_id")
        val dayOfWeek: DayOfWeek,

        @ManyToOne
        @JoinColumn(name = "store_id")
        val store: Store
)