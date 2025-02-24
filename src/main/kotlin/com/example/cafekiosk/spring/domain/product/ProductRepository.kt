package com.example.cafekiosk.spring.domain.product

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository : JpaRepository<Product, Long> {

    fun findBySellingStatusIn(
        sellingStatuses: List<Product.SellingStatus>
    ): List<Product>

    fun findByProductNumberIn(productNumbers: Collection<String>): List<Product>

    @Query("select p.productNumber from Product p order by p.id desc limit 1")
    fun findLatestProductNumber(): String?
}
