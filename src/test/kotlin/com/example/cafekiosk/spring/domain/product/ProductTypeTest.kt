package com.example.cafekiosk.spring.domain.product

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class ProductTypeTest {

    @ParameterizedTest
    @CsvSource(
        value = [
            "HANDMADE, false",
            "BOTTLE, true",
            "BAKERY, true",
        ]
    )
    fun `상품 타입이 재고 관련 타입인지 확인한다`(
        productType: Product.Type,
        expected: Boolean
    ) {
        // when
        val result = Product.Type.STOCK_TYPES.contains(productType)

        // then
        assertThat(result).isEqualTo(expected)
    }
}
