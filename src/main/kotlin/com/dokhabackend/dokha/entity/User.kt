package com.dokhabackend.dokha.entity

import lombok.Builder
import javax.persistence.*


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

@Entity
@Table(name = "user")
@Builder
data class User(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,

        @Column(name = "first_name")
        val firstName: String,

        @Column(name = "last_name")
        val lastName: String
)