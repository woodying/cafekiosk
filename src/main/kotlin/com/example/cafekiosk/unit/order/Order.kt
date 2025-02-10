package com.example.cafekiosk.unit.order

import com.example.cafekiosk.unit.beverage.Beverage
import java.time.LocalDateTime

data class Order(
    val orderDateTime: LocalDateTime,
    val beverages: List<Beverage>,
)
