package com.example.company.common.config

import kotlinx.serialization.json.Json
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter

/**
 * Configuration for kotlinx-serialization support in Spring MVC.
 *
 * This replaces the default Jackson serialization with kotlinx-serialization
 * for better Kotlin integration and type safety.
 */
@Configuration
class SerializationConfig {

    /**
     * Configure the Json serializer with sensible defaults.
     */
    @Bean
    fun json(): Json = Json {
        prettyPrint = true
        isLenient = false
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    /**
     * HTTP message converter using kotlinx-serialization.
     */
    @Bean
    fun kotlinSerializationJsonHttpMessageConverter(json: Json): KotlinSerializationJsonHttpMessageConverter {
        return KotlinSerializationJsonHttpMessageConverter(json)
    }
}
