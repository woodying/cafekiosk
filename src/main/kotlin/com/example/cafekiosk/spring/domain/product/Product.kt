package com.example.cafekiosk.spring.domain.product

import com.example.cafekiosk.spring.domain.BaseEntity
import jakarta.persistence.*
import java.time.LocalDateTime

@Table(name = "product")
data class Product(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val productNumber: String,

    @Enumerated(EnumType.STRING)
    val type: Type,

    @Enumerated(EnumType.STRING)
    val sellingType: SellingType,

    val name: String,
    val price: Int,
    override val createdAt: LocalDateTime? = null,
    override val modifiedAt: LocalDateTime? = null,
) : BaseEntity(createdAt, modifiedAt) {
    enum class Type(val description: String) {
        HANDMADE("제조 음료"),
        BOTTLE("병 음료"),
        BAKERY("베이커리");
    }

    enum class SellingType(val description: String) {
        SELLING("판매중"),
        HOLD("판매 보류"),
        STOP_SELLING("판매 중지");

        companion object {
            fun forDisplay(): List<SellingType> {
                return listOf(SELLING, HOLD)
            }
        }
    }
}
