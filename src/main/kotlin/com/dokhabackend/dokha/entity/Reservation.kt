package com.dokhabackend.dokha.entity

import com.dokhabackend.dokha.entity.dictionary.PlaceReservation
import java.time.LocalDateTime
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
        val user: User?,

        @Column(name = "reservation_start_time")
        val reservationStartTime: LocalDateTime,

        @Column(name = "reservation_end_time")
        val reservationEndTime: LocalDateTime,

        @Column(name = "closed")
        val closed: Boolean
)