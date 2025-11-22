package com.example.company.domain.product.service

import com.example.company.common.exception.ResourceNotFoundException
import com.example.company.domain.product.model.CreateProductRequest
import com.example.company.domain.product.model.Product
import com.example.company.domain.product.model.UpdateProductRequest
import com.example.company.domain.product.repository.ProductRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.Instant

private val logger = KotlinLogging.logger {}

/**
 * Service for managing products.
 */
@Service
@Transactional
class ProductService(
    private val productRepository: ProductRepository
) {

    /**
     * Create a new product.
     */
    fun createProduct(request: CreateProductRequest): Product {
        val product = Product(
            name = request.name,
            description = request.description,
            price = BigDecimal(request.price),
            stock = request.stock,
            available = request.stock > 0
        )

        return productRepository.save(product).also {
            logger.info { "Created product with id: ${it.id}" }
        }
    }

    /**
     * Get product by ID.
     */
    @Transactional(readOnly = true)
    fun getProductById(id: Long): Product {
        return productRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Product not found with id: $id") }
    }

    /**
     * Get all products.
     */
    @Transactional(readOnly = true)
    fun getAllProducts(): List<Product> {
        return productRepository.findAll()
    }

    /**
     * Get all available products.
     */
    @Transactional(readOnly = true)
    fun getAvailableProducts(): List<Product> {
        return productRepository.findByAvailableTrue()
    }

    /**
     * Search products by name.
     */
    @Transactional(readOnly = true)
    fun searchProductsByName(name: String): List<Product> {
        return productRepository.findByNameContainingIgnoreCase(name)
    }

    /**
     * Update product.
     */
    fun updateProduct(id: Long, request: UpdateProductRequest): Product {
        val product = getProductById(id)

        val updatedProduct = product.copy(
            name = request.name ?: product.name,
            description = request.description ?: product.description,
            price = request.price?.let { BigDecimal(it) } ?: product.price,
            stock = request.stock ?: product.stock,
            available = request.available ?: product.available,
            updatedAt = Instant.now()
        )

        return productRepository.save(updatedProduct).also {
            logger.info { "Updated product with id: $id" }
        }
    }

    /**
     * Delete product.
     */
    fun deleteProduct(id: Long) {
        val product = getProductById(id)
        productRepository.delete(product)
        logger.info { "Deleted product with id: $id" }
    }

    /**
     * Update product stock.
     */
    fun updateStock(id: Long, quantity: Int): Product {
        val product = getProductById(id)

        val newStock = product.stock + quantity
        require(newStock >= 0) { "Insufficient stock for product $id" }

        val updatedProduct = product.copy(
            stock = newStock,
            available = newStock > 0,
            updatedAt = Instant.now()
        )

        return productRepository.save(updatedProduct).also {
            logger.info { "Updated stock for product $id: ${product.stock} -> $newStock" }
        }
    }
}
