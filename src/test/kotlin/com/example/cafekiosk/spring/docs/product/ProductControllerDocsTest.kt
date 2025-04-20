package com.example.cafekiosk.spring.docs.product

import com.example.cafekiosk.spring.api.controller.product.ProductController
import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest
import com.example.cafekiosk.spring.api.service.product.ProductService
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse
import com.example.cafekiosk.spring.docs.RestDocsSupport
import com.example.cafekiosk.spring.domain.product.Product
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

class ProductControllerDocsTest : RestDocsSupport() {

    @MockkBean
    private lateinit var productService: ProductService

    override fun initController(): Any {
        return ProductController(productService)
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
            id = 0L,
            productNumber = "001",
            type = request.productType,
            sellingStatus = request.sellingStatus,
            name = request.name,
            price = request.price,
            createdAt = LocalDateTime.of(2025, 4, 19, 0, 0, 0),
            modifiedAt = LocalDateTime.of(2025, 4, 19, 0, 0, 0),
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
            .andDo(
                document(
                    "product-create",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("productType").description("상품 타입"),
                        fieldWithPath("name").optional().description("상품 이름"),
                        fieldWithPath("price").description("상품 가격"),
                        fieldWithPath("sellingStatus").description("판매 상태")
                    ),
                    responseFields(
                        fieldWithPath("code").description("응답 코드"),
                        fieldWithPath("message").description("응답 메시지"),
                        fieldWithPath("status").description("응답 상태"),
                        fieldWithPath("data.id").description("상품 아이디"),
                        fieldWithPath("data.productNumber").description("상품 번호"),
                        fieldWithPath("data.type").description("상품 타입"),
                        fieldWithPath("data.sellingStatus").description("판매 상태"),
                        fieldWithPath("data.name").description("상품 이름"),
                        fieldWithPath("data.price").description("상품 가격"),
                        fieldWithPath("data.createdAt").description("생성 일시"),
                        fieldWithPath("data.modifiedAt").description("수정 일시")
                    ),
                )
            )
    }
}
