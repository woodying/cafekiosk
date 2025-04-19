package com.example.cafekiosk.spring

import com.example.cafekiosk.spring.api.controller.order.OrderController
import com.example.cafekiosk.spring.api.controller.product.ProductController
import com.example.cafekiosk.spring.api.service.order.OrderService
import com.example.cafekiosk.spring.api.service.product.ProductService
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc

@WebMvcTest(
    OrderController::class,
    ProductController::class
)
abstract class ControllerTestSupport {

    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Autowired
    protected lateinit var objectMapper: ObjectMapper

    @MockkBean
    protected lateinit var orderService: OrderService

    @MockkBean
    protected lateinit var productService: ProductService
}
