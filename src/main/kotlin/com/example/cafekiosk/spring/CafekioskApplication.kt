package com.example.cafekiosk.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CafekioskApplication

fun main(args: Array<String>) {
    runApplication<CafekioskApplication>(*args)
}
