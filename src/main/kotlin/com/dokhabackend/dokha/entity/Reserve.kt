package com.dokhabackend.dokha.entity

import com.dokhabackend.dokha.entity.dictionary.PlaceReservation
import lombok.Builder
import java.sql.Timestamp
import javax.persistence.*

/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

@Entity
@Table(name = "reserve")
@Builder
data class Reserve(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @ManyToOne
        @JoinColumn(name = "place_id")
        val placeReservation: PlaceReservation,

        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,

        @Column(name = "reserve_time")
        val reserveTime: Timestamp
)