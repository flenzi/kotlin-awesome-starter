package com.example.company.domain.user.repository

import com.example.company.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Repository for User entity.
 */
@Repository
interface UserRepository : JpaRepository<User, Long> {

    /**
     * Find user by email address.
     */
    fun findByEmail(email: String): User?

    /**
     * Find all active users.
     */
    fun findByActiveTrue(): List<User>

    /**
     * Check if a user exists with the given email.
     */
    fun existsByEmail(email: String): Boolean
}
