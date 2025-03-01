package com.example.cafekiosk.spring.api.controller.product.request

import com.example.cafekiosk.spring.domain.product.Product
import jakarta.validation.constraints.Min

data class ProductCreateRequest(
    val productType: Product.Type,
    val sellingStatus: Product.SellingStatus,
    val name: String,
    @field:Min(value = 0, message = "price must be greater than or equal to 0")
    val price: Int
)
