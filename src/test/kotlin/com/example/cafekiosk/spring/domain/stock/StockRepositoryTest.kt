package com.example.cafekiosk.spring.domain.stock

import com.example.cafekiosk.spring.IntegrationTestSupport
import jakarta.transaction.Transactional
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple.tuple
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import kotlin.jvm.optionals.getOrNull

@Transactional
class StockRepositoryTest(
    @Autowired private val stockRepository: StockRepository
) : IntegrationTestSupport() {

    @DisplayName("상품번호 리스트로 재고를 조회한다.")
    @Test
    fun findByProductNumberIn() {
        // given
        val stock1 = Stock(
            productNumber = "001",
            quantity = 1
        )
        val stock2 = Stock(
            productNumber = "002",
            quantity = 2
        )
        val stock3 = Stock(
            productNumber = "003",
            quantity = 3
        )

        stockRepository.saveAll(listOf(stock1, stock2, stock3))

        // when
        val stocks = stockRepository.findByProductNumberIn(listOf("001", "002"))

        // then
        assertThat(stocks)
            .hasSize(2)
            .extracting("productNumber", "quantity")
            .containsExactlyInAnyOrder(
                tuple("001", 1),
                tuple("002", 2),
            )
    }

    @DisplayName("재고의 수량을 정상적으로 증감/차감 한다.")
    @Test
    fun incQuantityById() {
        // given
        val stock1 = Stock(
            productNumber = "001",
            quantity = 1
        )
        val stock2 = Stock(
            productNumber = "002",
            quantity = 2
        )
        stockRepository.saveAll(listOf(stock1, stock2))

        // when
        stockRepository.incrementQuantityById(stock1.id!!, 1)
        stockRepository.incrementQuantityById(stock2.id!!, -1)

        // then
        val updatedStock1 = stockRepository.findById(stock1.id!!).getOrNull()
        val updatedStock2 = stockRepository.findById(stock2.id!!).getOrNull()

        assertThat(updatedStock1?.quantity).isEqualTo(2)
        assertThat(updatedStock2?.quantity).isEqualTo(1)
    }
}
