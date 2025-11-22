package com.example.company

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Main Spring Boot Application class for the Kotlin Awesome Starter.
 *
 * This template provides a production-ready foundation for Kotlin + Spring Boot projects with:
 * - Domain-Driven Design package structure
 * - kotlinx-serialization instead of Jackson
 * - Comprehensive testing with Konsist architecture tests
 * - Built-in security scanning and code coverage
 * - Docker support with multi-stage builds
 * - Automated dependency updates via Dependabot
 */
@SpringBootApplication
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
