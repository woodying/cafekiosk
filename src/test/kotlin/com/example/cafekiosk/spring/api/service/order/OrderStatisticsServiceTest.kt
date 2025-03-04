package com.example.cafekiosk.spring.api.service.order

import com.example.cafekiosk.spring.client.MailSendClient
import com.example.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository
import com.example.cafekiosk.spring.domain.order.Order
import com.example.cafekiosk.spring.domain.order.OrderRepository
import com.example.cafekiosk.spring.domain.orderproduct.OrderProductRepository
import com.example.cafekiosk.spring.domain.product.Product
import com.example.cafekiosk.spring.domain.product.ProductRepository
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertTrue

@SpringBootTest
class OrderStatisticsServiceTest {

    @Autowired
    private lateinit var orderStatisticsService: OrderStatisticsService

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Autowired
    private lateinit var orderProductRepository: OrderProductRepository

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var mailSendHistoryRepository: MailSendHistoryRepository

    @MockkBean
    private lateinit var mailSendClient: MailSendClient

    @AfterEach
    fun tearDown() {
        orderProductRepository.deleteAllInBatch()
        orderRepository.deleteAllInBatch()
        productRepository.deleteAllInBatch()
        mailSendHistoryRepository.deleteAllInBatch()
    }

    @DisplayName("결제완료 주문들의 총 매출액을 메일로 전송한다.")
    @Test
    fun sendOrderStatisticsMail() {
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
        val products = listOf(product1, product2, product3)
        productRepository.saveAll(products)

        val paymentCompletedOrder1 = createPaymentCompletedOrder(
            products = products,
            dateTime = LocalDateTime.of(2025, 3, 4, 12, 30, 0)
        )
        val paymentCompletedOrder2 = createPaymentCompletedOrder(
            products = products,
            dateTime = LocalDateTime.of(2025, 3, 5, 12, 30, 0)
        )
        val paymentCompletedOrder3 = createPaymentCompletedOrder(
            products = products,
            dateTime = LocalDateTime.of(2025, 3, 6, 12, 30, 0)
        )

        every { mailSendClient.send(any(), any(), any(), any()) } returns true

        // when
        val result = orderStatisticsService.sendOrderStatisticsMail(
            email = "test@test.com",
            orderDate = LocalDate.of(2025, 3, 5)
        )

        // then
        assertTrue(result)

        val histories = mailSendHistoryRepository.findAll()
        assertThat(histories)
            .hasSize(1)
            .extracting("content")
            .contains("총 매출 합계는 ${paymentCompletedOrder2.totalPrice}원 입니다.")
    }

    private fun createPaymentCompletedOrder(
        products: List<Product>,
        dateTime: LocalDateTime
    ): Order {
        val order = Order.create(
            products = products,
            registeredDateTime = dateTime
        ).apply { status = Order.Status.PAYMENT_COMPLETED }

        return orderRepository.save(order)
    }
}
