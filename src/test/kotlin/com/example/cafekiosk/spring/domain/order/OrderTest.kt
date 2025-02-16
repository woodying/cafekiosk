package com.example.cafekiosk.spring.domain.order

import com.example.cafekiosk.spring.domain.product.Product
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class OrderTest {

    @DisplayName("주문 생성 시, 상품 리스트에서 주문의 총 금액을 계산한다.")
    @Test
    fun calculateTotalPrice() {
        // when
        val products = listOf(
            Product(
                productNumber = "001",
                type = Product.Type.HANDMADE,
                sellingStatus = Product.SellingStatus.SELLING,
                name = "아메리카노",
                price = 1000
            ),
            Product(
                productNumber = "002",
                type = Product.Type.HANDMADE,
                sellingStatus = Product.SellingStatus.SELLING,
                name = "아메리카노",
                price = 2000
            )
        )

        //when
        val order = Order.create(products, LocalDateTime.now())

        // then
        assertThat(order.totalPrice).isEqualTo(3000)
    }

    @DisplayName("주문 생성 시, 주문 상태는 INIT 이다.")
    @Test
    fun initOrder() {
        // when
        val products = listOf(
            Product(
                productNumber = "001",
                type = Product.Type.HANDMADE,
                sellingStatus = Product.SellingStatus.SELLING,
                name = "아메리카노",
                price = 1000
            ),
            Product(
                productNumber = "002",
                type = Product.Type.HANDMADE,
                sellingStatus = Product.SellingStatus.SELLING,
                name = "아메리카노",
                price = 2000
            )
        )

        //when
        val order = Order.create(products, LocalDateTime.now())

        // then
        assertThat(order.status).isEqualByComparingTo(Order.Status.INIT)
    }

    @DisplayName("주문 생성 시, 주문 등록 시간을 기록한다.")
    @Test
    fun registeredDateTime() {
        // when
        val products = listOf(
            Product(
                productNumber = "001",
                type = Product.Type.HANDMADE,
                sellingStatus = Product.SellingStatus.SELLING,
                name = "아메리카노",
                price = 1000
            ),
            Product(
                productNumber = "002",
                type = Product.Type.HANDMADE,
                sellingStatus = Product.SellingStatus.SELLING,
                name = "아메리카노",
                price = 2000
            )
        )
        val registeredDateTime = LocalDateTime.now()

        //when
        val order = Order.create(products, registeredDateTime)

        // then
        assertThat(order.registeredDateTime).isEqualTo(registeredDateTime)
    }
}
