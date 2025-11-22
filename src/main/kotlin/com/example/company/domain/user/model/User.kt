package com.example.company.domain.user.model

import jakarta.persistence.*
import kotlinx.serialization.Serializable
import java.time.Instant

/**
 * User entity representing a user in the system.
 */
@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false)
    val active: Boolean = true,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    val updatedAt: Instant = Instant.now()
)

/**
 * DTO for creating a new user.
 */
@Serializable
data class CreateUserRequest(
    val email: String,
    val name: String
)

/**
 * DTO for updating a user.
 */
@Serializable
data class UpdateUserRequest(
    val name: String? = null,
    val active: Boolean? = null
)

/**
 * DTO for user response.
 */
@Serializable
data class UserResponse(
    val id: Long,
    val email: String,
    val name: String,
    val active: Boolean,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(user: User): UserResponse = UserResponse(
            id = user.id!!,
            email = user.email,
            name = user.name,
            active = user.active,
            createdAt = user.createdAt.toString(),
            updatedAt = user.updatedAt.toString()
        )
    }
}
