package com.example.company.domain.product.model

import com.example.company.common.serialization.BigDecimalSerializer
import com.example.company.common.serialization.InstantSerializer
import com.example.company.common.serialization.UUIDSerializer
import com.example.company.common.util.UuidGenerator
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

/**
 * Product entity representing a product in the system.
 * Uses UUID v7 for time-ordered, distributed-system-friendly IDs.
 */
@Entity
@Table(name = "products")
data class Product(
    @Id
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "UUID")
    val id: UUID = UuidGenerator.generate(),

    @Column(nullable = false)
    val name: String,

    @Column(length = 1000)
    val description: String? = null,

    @Column(nullable = false, precision = 19, scale = 2)
    val price: BigDecimal,

    @Column(nullable = false)
    val stock: Int = 0,

    @Column(nullable = false)
    val available: Boolean = true,

    @Column(nullable = false, updatable = false)
    val createdAt: Instant = Instant.now(),

    @Column(nullable = false)
    val updatedAt: Instant = Instant.now()
)

/**
 * DTO for creating a new product.
 */
@Serializable
data class CreateProductRequest(
    val name: String,
    val description: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
    val stock: Int = 0
)

/**
 * DTO for updating a product.
 */
@Serializable
data class UpdateProductRequest(
    val name: String? = null,
    val description: String? = null,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal? = null,
    val stock: Int? = null,
    val available: Boolean? = null
)

/**
 * DTO for product response.
 */
@Serializable
data class ProductResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val description: String?,
    @Serializable(with = BigDecimalSerializer::class)
    val price: BigDecimal,
    val stock: Int,
    val available: Boolean,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant
) {
    companion object {
        fun from(product: Product): ProductResponse = ProductResponse(
            id = product.id,
            name = product.name,
            description = product.description,
            price = product.price,
            stock = product.stock,
            available = product.available,
            createdAt = product.createdAt,
            updatedAt = product.updatedAt
        )
    }
}
