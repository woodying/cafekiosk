package com.example.cafekiosk.spring.api.controller.product.request

import com.example.cafekiosk.spring.domain.product.Product

data class ProductCreateRequest(
    val productType: Product.Type,
    val sellingStatus: Product.SellingStatus,
    val name: String,
    val price: Int
)
