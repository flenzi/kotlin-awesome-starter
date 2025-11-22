package com.example.company.domain.product.repository

import com.example.company.domain.product.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.math.BigDecimal

/**
 * Repository for Product entity.
 */
@Repository
interface ProductRepository : JpaRepository<Product, Long> {

    /**
     * Find all available products.
     */
    fun findByAvailableTrue(): List<Product>

    /**
     * Find products by name containing (case-insensitive).
     */
    fun findByNameContainingIgnoreCase(name: String): List<Product>

    /**
     * Find products with price less than or equal to the given amount.
     */
    fun findByPriceLessThanEqual(price: BigDecimal): List<Product>

    /**
     * Find products with stock greater than zero.
     */
    fun findByStockGreaterThan(stock: Int): List<Product>
}
