package com.example.cafekiosk.spring.api.service.product

import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest
import com.example.cafekiosk.spring.domain.product.Product
import com.example.cafekiosk.spring.domain.product.ProductRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ProductServiceTest {

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var productRepository: ProductRepository

    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    fun create() {
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
        val request = ProductCreateRequest(
            productType = Product.Type.HANDMADE,
            name = "카푸치노",
            price = 5000,
            sellingStatus = Product.SellingStatus.SELLING
        )

        // when
        val productResponse = productService.create(request)

        // then
        assertThat(productResponse)
            .extracting("productNumber", "type", "name", "price", "sellingStatus")
            .contains("004", Product.Type.HANDMADE, "카푸치노", 5000, Product.SellingStatus.SELLING)
    }

    @DisplayName("상품번호를 1 증가시킨다.")
    @Test
    fun incProductNumber() {
        // given
        val function = ProductService::class
            .declaredFunctions
            .first { it.name == "incProductNumber" }
            .also { it.isAccessible = true }

        val latestProductNumber1 = "988"
        val latestProductNumber2 = null

        // when
        val nextProductNumber1 = function.call(productService, latestProductNumber1)
        val nextProductNumber2 = function.call(productService, latestProductNumber2)

        // then
        assertThat(nextProductNumber1).isEqualTo("989")
        assertThat(nextProductNumber2).isEqualTo("000")
    }
}
