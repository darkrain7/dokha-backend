package com.dokhabackend.dokha.entity

import lombok.Builder
import javax.persistence.*


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

@Entity
@Table(name = "user", schema = "dokha")
@Builder
data class User(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long,

        @Column(name = "login")
        val login: String,

        @Column(name = "password")
        val password: String,

        @OneToOne
        @JoinColumn(name = "role_id", referencedColumnName = "id")
        val role: Role
)