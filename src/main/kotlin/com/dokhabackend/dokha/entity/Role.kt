package com.dokhabackend.dokha.entity

import lombok.Builder
import javax.persistence.*


/**
 * Semenov A.E.
 * Created 19.05.2019
 *
 **/
@Entity
@Table(name = "role", schema = "dokha")
@Builder
data class Role(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Int,

        @Column(name = "name")
        val name: String,

        @Column(name = "alias")
        var alias: String
)