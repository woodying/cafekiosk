package com.example.cafekiosk.spring.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

    fun findBySellingStatusIn(
        sellingStatuses: List<Product.SellingStatus>
    ): List<Product>

    fun findByProductNumberIn(productNumbers: Collection<String>): List<Product>
}
