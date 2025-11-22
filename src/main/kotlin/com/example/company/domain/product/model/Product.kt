package com.example.company.domain.product.model

import jakarta.persistence.*
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.Instant

/**
 * Product entity representing a product in the system.
 */
@Entity
@Table(name = "products")
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

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
    val price: String, // String to handle BigDecimal serialization
    val stock: Int = 0
)

/**
 * DTO for updating a product.
 */
@Serializable
data class UpdateProductRequest(
    val name: String? = null,
    val description: String? = null,
    val price: String? = null,
    val stock: Int? = null,
    val available: Boolean? = null
)

/**
 * DTO for product response.
 */
@Serializable
data class ProductResponse(
    val id: Long,
    val name: String,
    val description: String?,
    val price: String,
    val stock: Int,
    val available: Boolean,
    val createdAt: String,
    val updatedAt: String
) {
    companion object {
        fun from(product: Product): ProductResponse = ProductResponse(
            id = product.id!!,
            name = product.name,
            description = product.description,
            price = product.price.toString(),
            stock = product.stock,
            available = product.available,
            createdAt = product.createdAt.toString(),
            updatedAt = product.updatedAt.toString()
        )
    }
}
