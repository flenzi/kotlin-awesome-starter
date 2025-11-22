package com.example.company.domain.user.controller

import com.example.company.domain.user.model.CreateUserRequest
import com.example.company.domain.user.model.UpdateUserRequest
import com.example.company.domain.user.model.UserResponse
import com.example.company.domain.user.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * REST controller for user operations.
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "User management APIs")
class UserController(
    private val userService: UserService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new user")
    fun createUser(@RequestBody request: CreateUserRequest): UserResponse {
        val user = userService.createUser(request)
        return UserResponse.from(user)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    fun getUserById(@PathVariable id: Long): UserResponse {
        val user = userService.getUserById(id)
        return UserResponse.from(user)
    }

    @GetMapping
    @Operation(summary = "Get all users")
    fun getAllUsers(): List<UserResponse> {
        return userService.getAllUsers().map { UserResponse.from(it) }
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active users")
    fun getActiveUsers(): List<UserResponse> {
        return userService.getActiveUsers().map { UserResponse.from(it) }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody request: UpdateUserRequest
    ): UserResponse {
        val user = userService.updateUser(id, request)
        return UserResponse.from(user)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete user")
    fun deleteUser(@PathVariable id: Long) {
        userService.deleteUser(id)
    }
}
