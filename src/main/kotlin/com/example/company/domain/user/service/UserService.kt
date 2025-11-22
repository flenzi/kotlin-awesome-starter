package com.example.company.domain.user.service

import com.example.company.common.exception.ResourceNotFoundException
import com.example.company.domain.user.model.CreateUserRequest
import com.example.company.domain.user.model.UpdateUserRequest
import com.example.company.domain.user.model.User
import com.example.company.domain.user.repository.UserRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.UUID

private val logger = KotlinLogging.logger {}

/**
 * Service for managing users.
 */
@Service
@Transactional
class UserService(
    private val userRepository: UserRepository
) {

    /**
     * Create a new user.
     */
    fun createUser(request: CreateUserRequest): User {
        require(!userRepository.existsByEmail(request.email)) {
            "User with email ${request.email} already exists"
        }

        val user = User(
            email = request.email,
            name = request.name
        )

        return userRepository.save(user).also {
            logger.info { "Created user with id: ${it.id}" }
        }
    }

    /**
     * Get user by ID.
     */
    @Transactional(readOnly = true)
    fun getUserById(id: UUID): User {
        return userRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("User not found with id: $id") }
    }

    /**
     * Get all users.
     */
    @Transactional(readOnly = true)
    fun getAllUsers(): List<User> {
        return userRepository.findAll()
    }

    /**
     * Get all active users.
     */
    @Transactional(readOnly = true)
    fun getActiveUsers(): List<User> {
        return userRepository.findByActiveTrue()
    }

    /**
     * Update user.
     */
    fun updateUser(id: UUID, request: UpdateUserRequest): User {
        val user = getUserById(id)

        val updatedUser = user.copy(
            name = request.name ?: user.name,
            active = request.active ?: user.active,
            updatedAt = Instant.now()
        )

        return userRepository.save(updatedUser).also {
            logger.info { "Updated user with id: $id" }
        }
    }

    /**
     * Delete user.
     */
    fun deleteUser(id: UUID) {
        val user = getUserById(id)
        userRepository.delete(user)
        logger.info { "Deleted user with id: $id" }
    }
}
