package com.example.cafekiosk.spring.domain.product

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest(
    @Autowired private val productRepository: ProductRepository,
) {

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    fun findBySellingStatus() {
        // given
        val product1 = Product(
            productNumber = "001",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.SELLING,
            name = "아메리카노",
            price = 4000
        )
        val product2 = Product(
            productNumber = "002",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.HOLD,
            name = "카페라떼",
            price = 4500
        )
        val product3 = Product(
            productNumber = "003",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.STOP_SELLING,
            name = "팥빙수",
            price = 7000
        )

        productRepository.saveAll(listOf(product1, product2, product3))

        // when
        val products = productRepository.findBySellingStatusIn(
            listOf(Product.SellingStatus.SELLING, Product.SellingStatus.HOLD)
        )

        // then
        assertThat(products)
            .hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "아메리카노", Product.SellingStatus.SELLING),
                tuple("002", "카페라떼", Product.SellingStatus.HOLD),
            )
    }

    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    fun findByProductNumberIn() {
        // given
        val product1 = Product(
            productNumber = "001",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.SELLING,
            name = "아메리카노",
            price = 4000
        )
        val product2 = Product(
            productNumber = "002",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.HOLD,
            name = "카페라떼",
            price = 4500
        )
        val product3 = Product(
            productNumber = "003",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.STOP_SELLING,
            name = "팥빙수",
            price = 7000
        )

        productRepository.saveAll(listOf(product1, product2, product3))

        // when
        val products = productRepository.findByProductNumberIn(
            listOf("001", "002")
        )

        // then
        assertThat(products)
            .hasSize(2)
            .extracting("productNumber", "name", "sellingStatus")
            .containsExactlyInAnyOrder(
                tuple("001", "아메리카노", Product.SellingStatus.SELLING),
                tuple("002", "카페라떼", Product.SellingStatus.HOLD),
            )
    }

    @DisplayName("가장 마지막 상품번호를 조회한다.")
    @Test
    fun findLatestProductNumber() {
        // given
        val targetProductNumber = "003"
        val product1 = Product(
            productNumber = "001",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.SELLING,
            name = "아메리카노",
            price = 4000
        )
        val product2 = Product(
            productNumber = "002",
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.HOLD,
            name = "카페라떼",
            price = 4500
        )
        val product3 = Product(
            productNumber = targetProductNumber,
            type = Product.Type.HANDMADE,
            sellingStatus = Product.SellingStatus.STOP_SELLING,
            name = "팥빙수",
            price = 7000
        )

        productRepository.saveAll(listOf(product1, product2, product3))

        // when
        val latestProductNumber = productRepository.findLatestProductNumber()

        // then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber)
    }
}
