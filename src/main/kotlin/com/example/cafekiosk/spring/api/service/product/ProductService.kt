package com.example.cafekiosk.spring.api.service.product

import com.example.cafekiosk.spring.api.service.product.response.ProductResponse
import com.example.cafekiosk.spring.domain.product.Product
import com.example.cafekiosk.spring.domain.product.ProductRepository
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository
) {
    suspend fun getSellingProducts(): List<ProductResponse> {
        val products = productRepository.findBySellingTypeIn(Product.SellingType.forDisplay())

        return products.map(ProductResponse::of).asFlow().toList()
    }
}
