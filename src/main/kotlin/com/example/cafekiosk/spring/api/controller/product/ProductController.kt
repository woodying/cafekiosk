package com.example.cafekiosk.spring.api.controller.product

import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest
import com.example.cafekiosk.spring.api.service.product.ProductService
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ProductController(
    private val productService: ProductService
) {

    @GetMapping("/api/v1/products/selling")
    fun getSellingProducts(): List<ProductResponse> {
        return productService.getSellingProducts()
    }

    @PostMapping("/api/v1/products/new")
    fun createProduct(
        @Valid @RequestBody request: ProductCreateRequest
    ): ProductResponse {
        return productService.create(request)
    }
}
