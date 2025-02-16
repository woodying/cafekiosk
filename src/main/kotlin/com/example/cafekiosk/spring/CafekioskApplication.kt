package com.example.cafekiosk.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class CafekioskApplication

fun main(args: Array<String>) {
    runApplication<CafekioskApplication>(*args)
}
