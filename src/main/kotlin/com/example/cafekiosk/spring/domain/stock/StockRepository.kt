package com.example.cafekiosk.spring.domain.stock

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface StockRepository : JpaRepository<Stock, Long> {
    fun findByProductNumberIn(productNumber: Collection<String>): List<Stock>

    @Modifying(clearAutomatically = true)
    @Query(
        "UPDATE Stock s " +
                "SET s.quantity = s.quantity + :inc " +
                "WHERE s.id = :id"
    )
    fun incrementQuantityById(id: Long, inc: Int): Int
}
