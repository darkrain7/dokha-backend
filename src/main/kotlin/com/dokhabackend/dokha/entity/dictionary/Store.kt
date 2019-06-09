package com.dokhabackend.dokha.entity.dictionary

import java.util.*
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
        val id: Long?,

        @Column(name = "name")
        val name: String,

        @Column(name = "location")
        val location: String
)