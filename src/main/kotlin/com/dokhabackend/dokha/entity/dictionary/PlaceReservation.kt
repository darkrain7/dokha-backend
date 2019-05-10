package com.dokhabackend.dokha.entity.dictionary

import lombok.Builder
import javax.persistence.*


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

@Entity
@Table(name = "s_place_reservation")
@Builder
data class PlaceReservation(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name = "description")
        val description: String,

        @ManyToOne
        @JoinColumn(name = "store_id")
        val store: Store
)