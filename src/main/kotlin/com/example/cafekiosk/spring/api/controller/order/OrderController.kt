package com.example.cafekiosk.spring.api.controller.order

import com.example.cafekiosk.spring.api.ApiResponse
import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest
import com.example.cafekiosk.spring.api.service.order.OrderService
import com.example.cafekiosk.spring.api.service.order.response.OrderResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
class OrderController(
    private val orderService: OrderService
) {

    @PostMapping("/api/v1/orders/new")
    fun createOrder(@RequestBody request: OrderCreateRequest): ApiResponse<OrderResponse> {
        return ApiResponse.ok(orderService.createOrder(request, LocalDateTime.now()))
    }
}
