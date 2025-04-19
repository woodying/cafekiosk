package com.example.cafekiosk.spring.docs

import com.example.cafekiosk.spring.api.controller.product.ProductController
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder

@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(
    controllers = [
        ProductController::class
    ]
)
abstract class RestDocsSupport {

    protected lateinit var mockMvc: MockMvc
    protected val objectMapper = ObjectMapper()

    @BeforeEach
    fun setUp(provider: RestDocumentationContextProvider) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(initController())
            .apply<StandaloneMockMvcBuilder>(documentationConfiguration(provider))
            .build()
    }

    protected abstract fun initController(): Any
}
