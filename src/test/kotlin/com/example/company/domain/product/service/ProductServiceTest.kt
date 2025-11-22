package com.example.company.domain.product.service

import com.example.company.common.exception.ResourceNotFoundException
import com.example.company.domain.product.model.CreateProductRequest
import com.example.company.domain.product.model.Product
import com.example.company.domain.product.model.UpdateProductRequest
import com.example.company.domain.product.repository.ProductRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal
import java.util.*

class ProductServiceTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var productService: ProductService

    @BeforeEach
    fun setup() {
        productRepository = mockk()
        productService = ProductService(productRepository)
    }

    @Test
    fun `should create product successfully`() {
        val request = CreateProductRequest(
            name = "Test Product",
            description = "Test Description",
            price = BigDecimal("99.99"),
            stock = 10
        )

        val savedProduct = Product(
            id = 1L,
            name = request.name,
            description = request.description,
            price = request.price,
            stock = request.stock,
            available = true
        )

        every { productRepository.save(any()) } returns savedProduct

        val result = productService.createProduct(request)

        assertEquals(savedProduct.id, result.id)
        assertEquals(savedProduct.name, result.name)
        assertEquals(savedProduct.price, result.price)
        assertTrue(result.available)

        verify { productRepository.save(any()) }
    }

    @Test
    fun `should get product by id successfully`() {
        val productId = 1L
        val product = Product(
            id = productId,
            name = "Test Product",
            price = BigDecimal("99.99"),
            stock = 10
        )

        every { productRepository.findById(productId) } returns Optional.of(product)

        val result = productService.getProductById(productId)

        assertEquals(product.id, result.id)
        assertEquals(product.name, result.name)
        verify { productRepository.findById(productId) }
    }

    @Test
    fun `should throw exception when product not found`() {
        val productId = 999L

        every { productRepository.findById(productId) } returns Optional.empty()

        assertThrows<ResourceNotFoundException> {
            productService.getProductById(productId)
        }

        verify { productRepository.findById(productId) }
    }

    @Test
    fun `should update product successfully`() {
        val productId = 1L
        val existingProduct = Product(
            id = productId,
            name = "Old Name",
            price = BigDecimal("50.00"),
            stock = 5
        )

        val updateRequest = UpdateProductRequest(
            name = "New Name",
            price = BigDecimal("75.00"),
            stock = 10
        )

        val updatedProduct = existingProduct.copy(
            name = updateRequest.name!!,
            price = updateRequest.price!!,
            stock = updateRequest.stock!!
        )

        every { productRepository.findById(productId) } returns Optional.of(existingProduct)
        every { productRepository.save(any()) } returns updatedProduct

        val result = productService.updateProduct(productId, updateRequest)

        assertEquals(updateRequest.name, result.name)
        assertEquals(updateRequest.price, result.price)
        assertEquals(updateRequest.stock, result.stock)

        verify { productRepository.findById(productId) }
        verify { productRepository.save(any()) }
    }

    @Test
    fun `should update stock successfully`() {
        val productId = 1L
        val product = Product(
            id = productId,
            name = "Test Product",
            price = BigDecimal("99.99"),
            stock = 10
        )

        val updatedProduct = product.copy(stock = 15)

        every { productRepository.findById(productId) } returns Optional.of(product)
        every { productRepository.save(any()) } returns updatedProduct

        val result = productService.updateStock(productId, 5)

        assertEquals(15, result.stock)
        verify { productRepository.findById(productId) }
        verify { productRepository.save(any()) }
    }

    @Test
    fun `should throw exception when reducing stock below zero`() {
        val productId = 1L
        val product = Product(
            id = productId,
            name = "Test Product",
            price = BigDecimal("99.99"),
            stock = 5
        )

        every { productRepository.findById(productId) } returns Optional.of(product)

        val exception = assertThrows<IllegalArgumentException> {
            productService.updateStock(productId, -10)
        }

        assertTrue(exception.message!!.contains("Insufficient stock"))
        verify { productRepository.findById(productId) }
        verify(exactly = 0) { productRepository.save(any()) }
    }

    @Test
    fun `should search products by name`() {
        val searchTerm = "test"
        val products = listOf(
            Product(id = 1L, name = "Test Product 1", price = BigDecimal("10.00")),
            Product(id = 2L, name = "Test Product 2", price = BigDecimal("20.00"))
        )

        every { productRepository.findByNameContainingIgnoreCase(searchTerm) } returns products

        val result = productService.searchProductsByName(searchTerm)

        assertEquals(2, result.size)
        verify { productRepository.findByNameContainingIgnoreCase(searchTerm) }
    }
}
