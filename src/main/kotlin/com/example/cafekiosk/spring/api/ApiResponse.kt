package com.example.cafekiosk.spring.api

import org.springframework.http.HttpStatus

data class ApiResponse<T>(
    val code: Int,
    val status: HttpStatus,
    val message: String? = null,
    val data: T?,
) {
    companion object {
        fun <T> ok(data: T?): ApiResponse<T> {
            return ApiResponse(
                code = 200,
                status = HttpStatus.OK,
                data = data,
            )
        }
    }
}
