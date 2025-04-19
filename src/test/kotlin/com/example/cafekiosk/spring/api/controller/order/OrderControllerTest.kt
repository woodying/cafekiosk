package com.example.cafekiosk.spring.api.controller.order

import com.example.cafekiosk.spring.ControllerTestSupport
import com.example.cafekiosk.spring.api.controller.order.request.OrderCreateRequest
import com.example.cafekiosk.spring.api.service.order.response.OrderResponse
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse
import com.example.cafekiosk.spring.domain.product.Product
import io.mockk.every
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.time.LocalDateTime

class OrderControllerTest : ControllerTestSupport() {

    @DisplayName("신규 주문을 등록한다.")
    @Test
    fun creteOrder() {
        // given
        val request = OrderCreateRequest(
            productNumbers = listOf("001")
        )
        every { orderService.createOrder(any(), any()) } returns OrderResponse(
            id = 1L,
            totalPrice = 4000,
            registeredDateTime = LocalDateTime.now(),
            products = listOf(
                ProductResponse(
                    productNumber = "001",
                    type = Product.Type.HANDMADE,
                    sellingStatus = Product.SellingStatus.SELLING,
                    name = "아메리카노",
                    price = 4000,
                    createdAt = null,
                    modifiedAt = null
                )
            )
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/orders/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
    }
}
