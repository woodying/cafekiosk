package com.example.cafekiosk.spring.domain.order

import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface OrderRepository : JpaRepository<Order, Long> {
    fun findByRegisteredDateTimeBetweenAndStatus(
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
        status: Order.Status
    ): List<Order>
}
