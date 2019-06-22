package com.dokhabackend.dokha.entity

import com.dokhabackend.dokha.entity.dictionary.PlaceReservation
import javax.persistence.*

/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

@Entity
@Table(name = "reservation", schema = "dokha")
data class Reservation(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @ManyToOne
        @JoinColumn(name = "place_id")
        val placeReservation: PlaceReservation,

        @ManyToOne
        @JoinColumn(name = "userId")
        val user: User,

        @ManyToOne
        @JoinColumn(name = "timetable_id")
        val timetable: Timetable?,

        @Column(name = "reservation_time")
        val reservationTime: Long
)