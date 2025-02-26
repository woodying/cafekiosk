package com.example.cafekiosk.spring.api.service.product

import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse
import com.example.cafekiosk.spring.domain.product.Product
import com.example.cafekiosk.spring.domain.product.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val productRepository: ProductRepository
) {

    @Transactional
    fun getSellingProducts(): List<ProductResponse> {
        return productRepository
            .findBySellingStatusIn(Product.SellingStatus.forDisplay())
            .map(ProductResponse::of)
    }

    fun create(request: ProductCreateRequest): ProductResponse {
        val latestProductNumber = productRepository.findLatestProductNumber()
        val product = productRepository.save(
            Product(
                type = request.productType,
                sellingStatus = request.sellingStatus,
                productNumber = incProductNumber(latestProductNumber),
                name = request.name,
                price = request.price
            )
        )

        return ProductResponse.of(product)
    }

    private fun incProductNumber(productNumber: String?): String {
        if (productNumber == null) {
            return "000"
        }

        return productNumber.toInt()
            .plus(1).toString()
            .padStart(3, '0')
    }
}
