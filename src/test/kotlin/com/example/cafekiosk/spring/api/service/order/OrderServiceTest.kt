package com.example.cafekiosk.spring.api.service.order

import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest
import com.example.cafekiosk.spring.domain.product.Product
import com.example.cafekiosk.spring.domain.product.ProductRepository
import com.example.cafekiosk.spring.domain.stock.Stock
import com.example.cafekiosk.spring.domain.stock.StockRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import kotlin.test.Test

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class OrderServiceTest {

    @Autowired
    private lateinit var productRepository: ProductRepository

    @Autowired
    private lateinit var stockRepository: StockRepository

    @Autowired
    private lateinit var orderService: OrderService

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

    @DisplayName("재고와 관련된 상품이 포함되어 있는 주문번호 리스트를 받아 주문을 생성한다.")
    @Test
    fun createOrderWithStock() {
        // given
        val product1 = Product(
            productNumber = "001",
            type = Product.Type.BOTTLE,
            sellingStatus = Product.SellingStatus.SELLING,
            name = "아메리카노",
            price = 1000
        )
        val product2 = Product(
            productNumber = "002",
            type = Product.Type.BAKERY,
            sellingStatus = Product.SellingStatus.HOLD,
            name = "빵",
            price = 3000
        )
        val product3 = Product(
            productNumber = "003",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.STOP_SELLING,
            name = "팥빙수",
            price = 5000
        )
        productRepository.saveAll(listOf(product1, product2, product3))

        val stock1 = Stock(
            productNumber = product1.productNumber,
            quantity = 2,
        )
        val stock2 = Stock(
            productNumber = product2.productNumber,
            quantity = 2,
        )
        stockRepository.saveAll(listOf(stock1, stock2))

        val request = OrderCreateRequest(productNumbers = listOf("001", "001", "002", "003"))
        val registeredDateTime = LocalDateTime.now()

        // when
        val orderResponse = orderService.createOrder(request, registeredDateTime)

        // then
        assertThat(orderResponse)
            .extracting("registeredDateTime", "totalPrice")
            .contains(registeredDateTime, 10000)

        assertThat(orderResponse.products)
            .hasSize(4)
            .extracting("productNumber", "price")
            .containsExactlyInAnyOrder(
                tuple("001", 1000),
                tuple("001", 1000),
                tuple("002", 3000),
                tuple("003", 5000)
            )

        val stocks = stockRepository.findAll()
        assertThat(stocks)
            .hasSize(2)
            .extracting("productNumber", "quantity")
            .containsExactlyInAnyOrder(
                tuple("001", 0),
                tuple("002", 1)
            )
    }

    @DisplayName("재고가 부족한 상품으로 주문을 생성하려는 경우, 예외가 발생한다.")
    @Test
    fun createOrderWithNoStock() {
        // given
        val product1 = Product(
            productNumber = "001",
            type = Product.Type.BOTTLE,
            sellingStatus = Product.SellingStatus.SELLING,
            name = "아메리카노",
            price = 1000
        )
        val product2 = Product(
            productNumber = "002",
            type = Product.Type.BAKERY,
            sellingStatus = Product.SellingStatus.HOLD,
            name = "빵",
            price = 3000
        )
        val product3 = Product(
            productNumber = "003",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.STOP_SELLING,
            name = "팥빙수",
            price = 5000
        )
        productRepository.saveAll(listOf(product1, product2, product3))

        val stock1 = Stock(
            productNumber = product1.productNumber,
            quantity = 1,
        )
        val stock2 = Stock(
            productNumber = product2.productNumber,
            quantity = 2,
        )
        stockRepository.saveAll(listOf(stock1, stock2))

        val request = OrderCreateRequest(productNumbers = listOf("001", "001", "002", "003"))
        val registeredDateTime = LocalDateTime.now()

        // when, then
        val exception = assertThrows<IllegalArgumentException> {
            orderService.createOrder(request, registeredDateTime)
        }

        assertThat(exception.message).isEqualTo("재고가 부족한 상품이 있습니다.")
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
