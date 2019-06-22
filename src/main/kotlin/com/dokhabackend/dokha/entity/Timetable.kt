package com.dokhabackend.dokha.entity

import com.dokhabackend.dokha.entity.dictionary.Store
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

        @Column(name = "start_date")
        val startDate: Long,

        @Column(name = "end_date")
        val endDate: Long,

        @Column(name = "working_date")
        val workingDate: Long,

        @Column(name = "working_day")
        val workingDay: Boolean,

        @ManyToOne
        @JoinColumn(name = "store_id")
        val store: Store
)