package com.example.cafekiosk.spring.api.service.product

import com.example.cafekiosk.spring.api.service.product.response.ProductResponse
import com.example.cafekiosk.spring.domain.product.Product
import com.example.cafekiosk.spring.domain.product.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    fun getSellingProducts(): List<ProductResponse> {
        return productRepository
            .findBySellingStatusIn(Product.SellingStatus.forDisplay())
            .map(ProductResponse::of)
    }
}
