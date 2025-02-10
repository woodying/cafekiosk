package com.example.cafekiosk.unit.beverage

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AmericanoTest {

    @Test
    fun getName() {
        val americano = Americano()

        assertEquals("아메리카노", americano.getName())
    }

    @Test
    fun getPrice() {
        val americano = Americano()

        assertEquals(4000, americano.getPrice())
    }
}
