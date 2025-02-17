package com.example.cafekiosk.spring.api.service.order

import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest
import com.example.cafekiosk.spring.domain.order.OrderRepository
import com.example.cafekiosk.spring.domain.orderproduct.OrderProductRepository
import com.example.cafekiosk.spring.domain.product.Product
import com.example.cafekiosk.spring.domain.product.ProductRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import kotlin.test.Test

@ActiveProfiles("test")
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderProductRepository: OrderProductRepository

    @Autowired
    private lateinit var orderService: OrderService


    @AfterEach
    fun tearDown() {
        orderProductRepository.deleteAllInBatch()
        productRepository.deleteAllInBatch()
        orderRepository.deleteAllInBatch()
    }

    @DisplayName("주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    fun createOrder() {
        // given
        val product1 = Product(
            productNumber = "001",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.SELLING,
            name = "아메리카노",
            price = 1000
        )
        val product2 = Product(
            productNumber = "002",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.HOLD,
            name = "카페라떼",
            price = 3000
        )
        val product3 = Product(
            productNumber = "003",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.STOP_SELLING,
            name = "팥빙수",
            price = 7000
        )
        productRepository.saveAll(listOf(product1, product2, product3))
        val request = OrderCreateRequest(productNumbers = listOf("001", "002"))
        val registeredDateTime = LocalDateTime.now()

        // when
        val orderResponse = orderService.createOrder(request, registeredDateTime)

        // then
        assertThat(orderResponse)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 4000)

        assertThat(orderResponse.products)
            .hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 1000),
                tuple("002", 3000)
            )
    }

    @DisplayName("중복되는 상품번호 리스트로 주문을 생성할 수 있다.")
    @Test
    fun createOrderWithDuplicateProducts() {
        // given
        val product1 = Product(
            productNumber = "001",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.SELLING,
            name = "아메리카노",
            price = 1000
        )
        val product2 = Product(
            productNumber = "002",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.HOLD,
            name = "카페라떼",
            price = 3000
        )
        val product3 = Product(
            productNumber = "003",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.STOP_SELLING,
            name = "팥빙수",
            price = 7000
        )
        productRepository.saveAll(listOf(product1, product2, product3))
        val request = OrderCreateRequest(productNumbers = listOf("001", "001"))
        val registeredDateTime = LocalDateTime.now()

        // when
        val orderResponse = orderService.createOrder(request, registeredDateTime)

        // then
        assertThat(orderResponse)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 2000)

        assertThat(orderResponse.products)
            .hasSize(2)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 1000),
                tuple("001", 1000)
            )
    }
}
