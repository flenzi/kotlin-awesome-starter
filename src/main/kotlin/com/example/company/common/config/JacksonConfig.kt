package com.example.company.common.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder

/**
 * Jackson configuration for Kotlin support.
 *
 * Configures Jackson with:
 * - Kotlin module for data classes, null safety, and default values
 * - Java Time module for Instant, LocalDate, etc.
 * - Pretty printing for better readability
 * - Proper handling of Kotlin's nullable types
 */
@Configuration
class JacksonConfig {

    @Bean
    fun objectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        return builder
            .createXmlMapper(false)
            .build<ObjectMapper>()
            .apply {
                registerKotlinModule()
                registerModule(JavaTimeModule())
                configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                configure(SerializationFeature.INDENT_OUTPUT, true)
            }
    }
}
