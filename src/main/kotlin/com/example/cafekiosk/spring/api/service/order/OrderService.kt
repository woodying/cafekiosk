package com.example.cafekiosk.spring.api.service.order

import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest
import com.example.cafekiosk.spring.api.service.order.response.OrderResponse
import com.example.cafekiosk.spring.domain.order.Order
import com.example.cafekiosk.spring.domain.order.OrderRepository
import com.example.cafekiosk.spring.domain.product.ProductRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) {
    fun createOrder(request: OrderCreateRequest, registeredDateTime: LocalDateTime): OrderResponse {
        val products = productRepository.findByProductNumberIn(request.productNumbers)

        val order = Order.create(
            products = products,
            registeredDateTime = registeredDateTime
        )

        val savedOrder = orderRepository.save(order)

        return OrderResponse.of(savedOrder)
    }
}
