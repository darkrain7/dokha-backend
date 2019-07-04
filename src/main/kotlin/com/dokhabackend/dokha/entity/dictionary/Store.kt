package com.dokhabackend.dokha.entity.dictionary

import javax.persistence.*


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

@Entity
@Table(name = "s_store", schema = "dokha")
data class Store(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(name = "name")
        val name: String,

        @Column(name = "location")
        val location: String,

        @Column(name = "photo")
        val photo: ByteArray,

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "store")
        val placeReservations: Collection<PlaceReservation>
)