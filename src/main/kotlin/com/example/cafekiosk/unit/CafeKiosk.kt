package com.example.cafekiosk.unit

import com.example.cafekiosk.unit.beverage.Beverage
import com.example.cafekiosk.unit.order.Order
import java.time.LocalDateTime

class CafeKiosk {

    private val beverages = mutableListOf<Beverage>()

    fun add(beverage: Beverage) {
        beverages.add(beverage)
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

    fun createOrder(): Order {
        return Order(LocalDateTime.now(), beverages)
    }
}
