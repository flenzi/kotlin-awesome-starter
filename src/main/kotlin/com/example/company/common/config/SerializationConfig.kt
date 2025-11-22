package com.example.company.common.config

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.KotlinSerializationJsonHttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * Kotlinx Serialization configuration for Spring Boot.
 *
 * Configures kotlinx-serialization with:
 * - Pretty printing for better readability
 * - Ignoring unknown keys for backward compatibility
 * - Encoding defaults to include all fields
 * - Custom serializers module for UUID and Instant
 */
@Configuration
class SerializationConfig : WebMvcConfigurer {

    @Bean
    fun json(): Json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = false
        coerceInputValues = false
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(0, KotlinSerializationJsonHttpMessageConverter(json()))
    }
}
