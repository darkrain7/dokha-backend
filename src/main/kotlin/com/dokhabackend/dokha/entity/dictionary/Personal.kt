package com.dokhabackend.dokha.entity.dictionary

import javax.persistence.*


/**
 * Semenov A.E.
 * Created 03.05.2019
 *
 **/

@Entity
@Table(name = "s_personal", schema = "dokha")
data class Personal(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,

        @Column(name = "first_name")
        val firstName: String,

        @Column(name = "last_name")
        val lastName: String,

        @Column(name = "age")
        val age: Int,

        @ManyToOne
        @JoinColumn(name = "position_id")
        val personalPosition: PersonalPosition

)