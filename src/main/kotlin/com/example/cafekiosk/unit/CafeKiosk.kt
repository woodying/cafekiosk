package com.example.cafekiosk.unit

import com.example.cafekiosk.unit.beverage.Beverage
import com.example.cafekiosk.unit.order.Order
import java.time.LocalDateTime
import java.time.LocalTime

class CafeKiosk {

    companion object {
        val shopOpenTime: LocalTime = LocalTime.of(10, 0)
        val shopCloseTime: LocalTime = LocalTime.of(22, 0)
    }

    private val beverages = mutableListOf<Beverage>()

    fun add(beverage: Beverage) {
        beverages.add(beverage)
    }

    fun add(beverage: Beverage, count: Int) {
        if (count <= 0) {
            throw IllegalArgumentException("음료는 1잔 이상 주문하실 수 있습니다.")
        }

        for (i in 1..count) {
            beverages.add(beverage)
        }
    }

    fun remove(beverage: Beverage) {
        beverages.remove(beverage)
    }

    fun calculateTotalPrice(): Int {
        return beverages.sumOf { it.getPrice() }
    }

    fun clear() {
        beverages.clear()
    }

    fun createOrder(currentDateTime: LocalDateTime): Order {
        val currentTime = currentDateTime.toLocalTime()

        if (currentTime.isBefore(shopOpenTime) || currentTime.isAfter(shopCloseTime)) {
            throw IllegalArgumentException("주문 시간이 아닙니다. 관리자에게 문의하세요.")
        }

        return Order(currentDateTime, beverages)
    }

    fun getBeverages(): List<Beverage> {
        return beverages
    }
}
