package com.example.company.domain.product.controller

import com.example.company.domain.product.model.CreateProductRequest
import com.example.company.domain.product.model.ProductResponse
import com.example.company.domain.product.model.UpdateProductRequest
import com.example.company.domain.product.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
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
        @PathVariable id: UUID,
        @RequestBody request: UpdateProductRequest
    ): ProductResponse {
        val product = productService.updateProduct(id, request)
        return ProductResponse.from(product)
    }

    @PatchMapping("/{id}/stock")
    @Operation(summary = "Update product stock")
    fun updateStock(
        @PathVariable id: UUID,
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
