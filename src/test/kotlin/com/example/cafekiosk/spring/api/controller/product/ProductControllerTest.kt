package com.example.cafekiosk.spring.api.controller.product

import com.example.cafekiosk.spring.ControllerTestSupport
import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest
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

class ProductControllerTest : ControllerTestSupport() {

    @DisplayName("판매 상품을 조회한다.")
    @Test
    fun getSellingProducts() {
        // given
        every { productService.getSellingProducts() } returns listOf(
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

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/v1/products/selling")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.status").value("OK"))
            .andExpect(jsonPath("$.data").isArray())
    }

    @DisplayName("신규 상품을 생성한다.")
    @Test
    fun createProduct() {
        // given
        val request = ProductCreateRequest(
            productType = Product.Type.HANDMADE,
            name = "아메리카노",
            price = 4000,
            sellingStatus = Product.SellingStatus.SELLING
        )
        every { productService.create(any()) } returns ProductResponse(
            productNumber = "001",
            type = request.productType,
            sellingStatus = request.sellingStatus,
            name = request.name,
            price = request.price,
            createdAt = null,
            modifiedAt = null
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @DisplayName("신규 상품을 생성할 때, 가격이 음수인 경우 예외를 발생시킨다.")
    @Test
    fun createProductWithMinusPrice() {
        // given
        val request = ProductCreateRequest(
            productType = Product.Type.HANDMADE,
            name = "아메리카노",
            price = -1000,
            sellingStatus = Product.SellingStatus.SELLING
        )
        every { productService.create(any()) } returns ProductResponse(
            productNumber = "001",
            type = request.productType,
            sellingStatus = request.sellingStatus,
            name = request.name,
            price = request.price,
            createdAt = null,
            modifiedAt = null
        )

        // when, then
        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/api/v1/products/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
            .andExpect(
                jsonPath("$.message")
                    .value("price must be greater than or equal to 0")
            )
    }
}
