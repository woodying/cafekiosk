package com.example.cafekiosk.spring.domain.product

import com.example.cafekiosk.spring.domain.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "product")
data class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val productNumber: String,

    @Enumerated(EnumType.STRING)
    val type: Type,

    @Enumerated(EnumType.STRING)
    val sellingStatus: SellingStatus,

    val name: String,
    val price: Int
) : BaseEntity() {
    enum class Type(val description: String) {
        HANDMADE("제조 음료"),
        BOTTLE("병 음료"),
        BAKERY("베이커리");

        companion object {
            val STOCK_TYPES = listOf(BOTTLE, BAKERY)
        }
    }

    enum class SellingStatus(val description: String) {
        SELLING("판매중"),
        HOLD("판매 보류"),
        STOP_SELLING("판매 중지");

        companion object {
            fun forDisplay(): List<SellingStatus> {
                return listOf(SELLING, HOLD)
            }
        }
    }
}
