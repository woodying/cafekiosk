package com.example.cafekiosk.unit.beverage

class Latte : Beverage {
    override fun getName(): String {
        return "라떼"
    }

    override fun getPrice(): Int {
        return 4500
    }
}
