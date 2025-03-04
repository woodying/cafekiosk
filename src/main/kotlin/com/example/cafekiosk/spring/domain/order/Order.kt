package com.example.cafekiosk.spring.domain.order

import com.example.cafekiosk.spring.domain.BaseEntity
import com.example.cafekiosk.spring.domain.orderproduct.OrderProduct
import com.example.cafekiosk.spring.domain.product.Product
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Enumerated(EnumType.STRING)
    var status: Status,

    val registeredDateTime: LocalDateTime,

    val totalPrice: Int,

    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL])
    val orderProducts: MutableList<OrderProduct> = mutableListOf(),
) : BaseEntity() {
    enum class Status(val description: String) {
        INIT("주문생성"),
        CANCELED("주문취소"),
        PAYMENT_COMPLETED("결제완료"),
        PAYMENT_FAILED("결제실패"),
        RECEIVED("주문접수"),
        COMPLETED("처리완료"),
    }

    companion object {
        fun create(products: List<Product>, registeredDateTime: LocalDateTime): Order {
            val order = Order(
                status = Status.INIT,
                registeredDateTime = registeredDateTime,
                totalPrice = products.sumOf { it.price },
            )

            order.orderProducts.addAll(
                products.map {
                    OrderProduct(
                        order = order,
                        product = it
                    )
                }
            )

            return order
        }
    }
}
