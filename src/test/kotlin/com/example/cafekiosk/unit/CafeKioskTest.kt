package com.example.cafekiosk.unit

import com.example.cafekiosk.unit.beverage.Americano
import com.example.cafekiosk.unit.beverage.Latte
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertEquals

class CafeKioskTest {

    @Test
    @DisplayName("음료 1개를 추가하면 주문목록에 담긴다.")
    fun add() {
        val cafeKiosk = CafeKiosk()

        cafeKiosk.add(Americano())

        assertEquals(1, cafeKiosk.getBeverages().size)
        assertEquals("아메리카노", cafeKiosk.getBeverages()[0].getName())
    }

    @Test
    @DisplayName("음료를 여러번 추가하면 모두 주문목록에 담긴다.")
    fun addMultiple() {
        val cafeKiosk = CafeKiosk()
        val americano = Americano()

        cafeKiosk.add(americano, 2)

        assertEquals(americano, cafeKiosk.getBeverages()[0])
        assertEquals(americano, cafeKiosk.getBeverages()[1])
        assertEquals(2, cafeKiosk.getBeverages().size)
    }

    @Test
    @DisplayName("음료를 0번 추가하면 오류가 발생한다.")
    fun addMultipleZero() {
        val cafeKiosk = CafeKiosk()
        val americano = Americano()

        val exception = assertThrows<IllegalArgumentException> {
            cafeKiosk.add(americano, 0)
        }

        assertEquals("음료는 1잔 이상 주문하실 수 있습니다.", exception.message)
    }

    @Test
    @DisplayName("음료를 삭제하면 주문목록에서 사라진다.")
    fun remove() {
        val cafeKiosk = CafeKiosk()
        val americano = Americano()

        cafeKiosk.add(americano)
        assertEquals(1, cafeKiosk.getBeverages().size)

        cafeKiosk.remove(americano)
        assertEquals(0, cafeKiosk.getBeverages().size)
    }

    @Test
    @DisplayName("clear() 호출 시 모든 주문목록이 초기화 된다.")
    fun clear() {
        val cafeKiosk = CafeKiosk()

        cafeKiosk.add(Americano())
        cafeKiosk.add(Latte())
        assertEquals(2, cafeKiosk.getBeverages().size)

        cafeKiosk.clear()
        assertEquals(0, cafeKiosk.getBeverages().size)
    }

    @Test
    @DisplayName("영업시간내에 주문을 생성하면 정상적으로 생성된다.")
    fun creteOrder() {
        val cafeKiosk = CafeKiosk()
        val americano = Americano()

        cafeKiosk.add(americano)

        val order = cafeKiosk.createOrder(
            LocalDateTime.of(2025, 1, 17, 10, 0, 0)
        )

        assertEquals(1, order.beverages.size)
        assertEquals("아메리카노", order.beverages[0].getName())
    }

    @Test
    @DisplayName("영업시간 외 주문을 생성하면 오류가 발생한다.")
    fun creteOrderClosedTime() {
        val cafeKiosk = CafeKiosk()
        val americano = Americano()

        cafeKiosk.add(americano)

        val exception = assertThrows<IllegalArgumentException> {
            cafeKiosk.createOrder(LocalDateTime.of(2025, 1, 17, 9, 59))
        }

        assertEquals("주문 시간이 아닙니다. 관리자에게 문의하세요.", exception.message)
    }

    @Test
    @DisplayName("주문목록에 담긴 상품들의 총 금액을 계산할 수 있다.")
    fun calculateTotalPrice() {
        val cafeKiosk = CafeKiosk()
        val americano = Americano()
        val latte = Latte()

        cafeKiosk.add(americano)
        cafeKiosk.add(latte)

        val totalPrice = cafeKiosk.calculateTotalPrice()

        assertEquals(8500, totalPrice)
    }
}
