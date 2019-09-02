package com.dokhabackend.dokha.entity.dictionary

import javax.persistence.*


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

@Entity
@Table(name = "s_place_reservation", schema = "dokha")
data class PlaceReservation(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(name = "description")
        val description: String,

        @Column(name = "seats_count")
        val seatsCount: Int,

        @Column(name = "have_game_console")
        val haveGamingConsole: Boolean,

        @Column(name = "image_id")
        val imageId: Long,

        @ManyToOne
        @JoinColumn(name = "store_id")
        val store: Store
)