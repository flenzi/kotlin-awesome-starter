package com.example.company.domain.user.service

import com.example.company.common.exception.ResourceNotFoundException
import com.example.company.domain.user.model.CreateUserRequest
import com.example.company.domain.user.model.UpdateUserRequest
import com.example.company.domain.user.model.User
import com.example.company.domain.user.repository.UserRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import java.util.Optional
import java.util.UUID

class UserServiceTest {

    private lateinit var userRepository: UserRepository
    private lateinit var userService: UserService

    @BeforeEach
    fun setup() {
        userRepository = mockk()
        userService = UserService(userRepository)
    }

    @Test
    fun `should create user successfully`() {
        val request = CreateUserRequest(
            email = "test@example.com",
            name = "Test User"
        )

        val savedUser = User(
            id = UUID.randomUUID(),
            email = request.email,
            name = request.name
        )

        every { userRepository.existsByEmail(request.email) } returns false
        every { userRepository.save(any()) } returns savedUser

        val result = userService.createUser(request)

        assertEquals(savedUser.id, result.id)
        assertEquals(savedUser.email, result.email)
        assertEquals(savedUser.name, result.name)

        verify { userRepository.existsByEmail(request.email) }
        verify { userRepository.save(any()) }
    }

    @Test
    fun `should throw exception when creating user with existing email`() {
        val request = CreateUserRequest(
            email = "existing@example.com",
            name = "Test User"
        )

        every { userRepository.existsByEmail(request.email) } returns true

        val exception = assertThrows<IllegalArgumentException> {
            userService.createUser(request)
        }

        assertTrue(exception.message!!.contains("already exists"))
        verify { userRepository.existsByEmail(request.email) }
        verify(exactly = 0) { userRepository.save(any()) }
    }

    @Test
    fun `should get user by id successfully`() {
        val userId = UUID.randomUUID()
        val user = User(
            id = userId,
            email = "test@example.com",
            name = "Test User"
        )

        every { userRepository.findById(userId) } returns Optional.of(user)

        val result = userService.getUserById(userId)

        assertEquals(user.id, result.id)
        assertEquals(user.email, result.email)
        verify { userRepository.findById(userId) }
    }

    @Test
    fun `should throw exception when user not found`() {
        val userId = UUID.randomUUID()

        every { userRepository.findById(userId) } returns Optional.empty()

        assertThrows<ResourceNotFoundException> {
            userService.getUserById(userId)
        }

        verify { userRepository.findById(userId) }
    }

    @Test
    fun `should update user successfully`() {
        val userId = UUID.randomUUID()
        val existingUser = User(
            id = userId,
            email = "test@example.com",
            name = "Old Name",
            active = true
        )

        val updateRequest = UpdateUserRequest(
            name = "New Name",
            active = false
        )

        val updatedUser = existingUser.copy(
            name = updateRequest.name!!,
            active = updateRequest.active!!,
            updatedAt = Instant.now()
        )

        every { userRepository.findById(userId) } returns Optional.of(existingUser)
        every { userRepository.save(any()) } returns updatedUser

        val result = userService.updateUser(userId, updateRequest)

        assertEquals(updateRequest.name, result.name)
        assertEquals(updateRequest.active, result.active)

        verify { userRepository.findById(userId) }
        verify { userRepository.save(any()) }
    }

    @Test
    fun `should delete user successfully`() {
        val userId = UUID.randomUUID()
        val user = User(
            id = userId,
            email = "test@example.com",
            name = "Test User"
        )

        every { userRepository.findById(userId) } returns Optional.of(user)
        every { userRepository.delete(user) } returns Unit

        userService.deleteUser(userId)

        verify { userRepository.findById(userId) }
        verify { userRepository.delete(user) }
    }

    @Test
    fun `should get all active users`() {
        val activeUsers = listOf(
            User(id = UUID.randomUUID(), email = "user1@example.com", name = "User 1", active = true),
            User(id = UUID.randomUUID(), email = "user2@example.com", name = "User 2", active = true)
        )

        every { userRepository.findByActiveTrue() } returns activeUsers

        val result = userService.getActiveUsers()

        assertEquals(2, result.size)
        assertTrue(result.all { it.active })

        verify { userRepository.findByActiveTrue() }
    }
}
