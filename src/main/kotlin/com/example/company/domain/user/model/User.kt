package com.example.company.domain.user.model

import com.example.company.common.util.UuidGenerator
import jakarta.persistence.*
import java.time.Instant
import java.util.UUID

/**
 * User entity representing a user in the system.
 * Uses UUID v7 for time-ordered, distributed-system-friendly IDs.
 */
@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    val id: UUID = UuidGenerator.generate(),

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
data class CreateUserRequest(
    val email: String,
    val name: String
)

/**
 * DTO for updating a user.
 */
data class UpdateUserRequest(
    val name: String? = null,
    val active: Boolean? = null
)

/**
 * DTO for user response.
 */
data class UserResponse(
    val id: UUID,
    val email: String,
    val name: String,
    val active: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant
) {
    companion object {
        fun from(user: User): UserResponse = UserResponse(
            id = user.id,
            email = user.email,
            name = user.name,
            active = user.active,
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}
