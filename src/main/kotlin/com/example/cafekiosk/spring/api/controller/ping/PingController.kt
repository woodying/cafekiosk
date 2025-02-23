package com.example.cafekiosk.spring.api.controller.ping

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class PingController {

    @GetMapping("/ping")
    fun ping() = "pong"
}
