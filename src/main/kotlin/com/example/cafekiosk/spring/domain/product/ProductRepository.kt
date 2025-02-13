package com.example.cafekiosk.spring.domain.product

import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
interface ProductRepository : ReactiveCrudRepository<Product, Long> {

    fun findBySellingTypeIn(
        sellingTypes: List<Product.SellingType>
    ): Flux<Product>
}
