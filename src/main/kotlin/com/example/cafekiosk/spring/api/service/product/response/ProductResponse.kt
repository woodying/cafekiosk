package com.example.cafekiosk.spring.api.service.product.response

import com.example.cafekiosk.spring.domain.product.Product
import com.example.cafekiosk.spring.domain.product.Product.SellingStatus
import com.example.cafekiosk.spring.domain.product.Product.Type
import java.time.LocalDateTime

data class ProductResponse(
    val id: Long? = null,
    val productNumber: String,
    val type: Type,
    val sellingStatus: SellingStatus,
    val name: String,
    val price: Int,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
) {
    companion object {
        fun of(product: Product): ProductResponse {
            return ProductResponse(
                id = product.id,
                productNumber = product.productNumber,
                type = product.type,
                sellingStatus = product.sellingStatus,
                name = product.name,
                price = product.price,
                createdAt = product.createdAt,
                modifiedAt = product.modifiedAt,
            )
        }
    }
}
