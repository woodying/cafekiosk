package com.example.cafekiosk.spring.api.service.product

import com.example.cafekiosk.spring.IntegrationTestSupport
import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest
import com.example.cafekiosk.spring.domain.product.Product
import com.example.cafekiosk.spring.domain.product.ProductRepository
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.isAccessible

@Transactional
class ProductServiceTest : IntegrationTestSupport() {

    @Autowired
    private lateinit var productService: ProductService

    @Autowired
    private lateinit var productRepository: ProductRepository

    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품의 상품번호에서 1 증가한 값이다.")
    @Test
    fun create() {
        // given
        val product = Product(
            productNumber = "001",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.SELLING,
            name = "아메리카노",
            price = 1000
        )
        productRepository.save(product)

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
            .contains("002", Product.Type.HANDMADE, "카푸치노", 5000, Product.SellingStatus.SELLING)

        val products = productRepository.findAll()
        assertThat(products)
            .hasSize(2)
            .extracting("productNumber", "name", "type", "price", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "아메리카노", Product.Type.HANDMADE, 1000, Product.SellingStatus.SELLING),
                tuple("002", "카푸치노", Product.Type.HANDMADE, 5000, Product.SellingStatus.SELLING)
            )
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
        assertThat(nextProductNumber2).isEqualTo("001")
    }
}
