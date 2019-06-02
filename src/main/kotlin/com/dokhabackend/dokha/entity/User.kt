package com.dokhabackend.dokha.entity

import com.dokhabackend.dokha.security.UserRoleEnum
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
        val id: Long = 0,

        @Column(name = "login")
        val login: String,

        @Column(name = "password")
        val password: String,

        @ElementCollection(targetClass = UserRoleEnum::class, fetch = FetchType.EAGER)
        @CollectionTable(name = "user_role", joinColumns = [JoinColumn(name = "user_id")])
        @Enumerated(EnumType.ORDINAL)
        val roles: Set<UserRoleEnum>
)