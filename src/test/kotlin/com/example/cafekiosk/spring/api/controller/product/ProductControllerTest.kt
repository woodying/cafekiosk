package com.example.cafekiosk.spring.api.controller.product

import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest
import com.example.cafekiosk.spring.api.service.product.ProductService
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse
import com.example.cafekiosk.spring.domain.product.Product
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath

@WebMvcTest(ProductController::class)
class ProductControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @MockkBean
    private lateinit var productService: ProductService

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
