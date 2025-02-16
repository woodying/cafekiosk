package com.example.cafekiosk.spring.domain.orderproduct

import com.example.cafekiosk.spring.domain.BaseEntity
import com.example.cafekiosk.spring.domain.order.Order
import com.example.cafekiosk.spring.domain.product.Product
import jakarta.persistence.*

@Entity
data class OrderProduct(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    val order: Order,

    @ManyToOne(fetch = FetchType.LAZY)
    val product: Product
) : BaseEntity()
