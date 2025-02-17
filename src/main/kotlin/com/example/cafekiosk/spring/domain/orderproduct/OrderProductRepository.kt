package com.example.cafekiosk.spring.domain.orderproduct

import org.springframework.data.jpa.repository.JpaRepository

interface OrderProductRepository : JpaRepository<OrderProduct, Long>
