package com.example.company.common.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * OpenAPI/Swagger configuration for API documentation.
 *
 * Access the Swagger UI at: http://localhost:8080/swagger-ui.html
 */
@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("Kotlin Awesome Starter API")
                .version("1.0.0")
                .description(
                    """
                    Production-ready Kotlin + Spring Boot template with:
                    - Domain-Driven Design
                    - kotlinx-serialization
                    - Comprehensive testing & architecture validation
                    - Security scanning & code coverage
                    - Docker support
                    """.trimIndent()
                )
                .license(
                    License()
                        .name("MIT")
                        .url("https://opensource.org/licenses/MIT")
                )
        )
}
