package com.dokhabackend.dokha.entity.dictionary

import lombok.Builder
import java.util.*
import javax.persistence.*


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 * Должности сотрудников
 *
 **/

@Entity
@Table(name = "s_personal_position")
@Builder
data class PersonalPosition(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(name = "name")
        val name: String
)