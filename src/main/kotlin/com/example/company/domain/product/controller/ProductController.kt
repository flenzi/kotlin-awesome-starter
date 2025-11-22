package com.example.company.domain.product.controller

import com.example.company.domain.product.model.CreateProductRequest
import com.example.company.domain.product.model.ProductResponse
import com.example.company.domain.product.model.UpdateProductRequest
import com.example.company.domain.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.UUID

/**
 * REST controller for product operations.
 */
@RestController
@RequestMapping("/api/products")
@Tag(name = "Products", description = "Product management APIs")
class ProductController(
    private val productService: ProductService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new product")
    fun createProduct(@RequestBody request: CreateProductRequest): ProductResponse {
        val product = productService.createProduct(request)
        return ProductResponse.from(product)
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID")
    fun getProductById(@PathVariable id: UUID): ProductResponse {
        val product = productService.getProductById(id)
        return ProductResponse.from(product)
    }

    @GetMapping
    @Operation(summary = "Get all products")
    fun getAllProducts(): List<ProductResponse> {
        return productService.getAllProducts().map { ProductResponse.from(it) }
    }

    @GetMapping("/available")
    @Operation(summary = "Get all available products")
    fun getAvailableProducts(): List<ProductResponse> {
        return productService.getAvailableProducts().map { ProductResponse.from(it) }
    }

    @GetMapping("/search")
    @Operation(summary = "Search products by name")
    fun searchProducts(@RequestParam name: String): List<ProductResponse> {
        return productService.searchProductsByName(name).map { ProductResponse.from(it) }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update product")
    fun updateProduct(
        @PathVariable id: Long,
        @RequestBody request: UpdateProductRequest
    ): ProductResponse {
        val product = productService.updateProduct(id, request)
        return ProductResponse.from(product)
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update product stock")
    fun updateStock(
        @PathVariable id: Long,
        @RequestBody request: UpdateStockRequest
    ): ProductResponse {
        val product = productService.updateStock(id, request.quantity)
        return ProductResponse.from(product)
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete product")
    fun deleteProduct(@PathVariable id: UUID) {
        productService.deleteProduct(id)
    }
}

data class UpdateStockRequest(
    val quantity: Int
)
