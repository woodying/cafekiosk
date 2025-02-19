package com.example.cafekiosk.spring.domain.stock

import com.example.cafekiosk.spring.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class Stock(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val productNumber: String,

    val quantity: Int,
) : BaseEntity() {

    fun isQuantityLessThan(quantity: Int): Boolean {
        return this.quantity < quantity
    }
}
