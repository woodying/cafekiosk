package com.example.cafekiosk.spring.api

import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ApiControllerAdvice {

    @ExceptionHandler(BindException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBindException(e: BindException): ApiResponse<*> {
        return ApiResponse(
            code = 400,
            status = HttpStatus.BAD_REQUEST,
            message = e.allErrors[0].defaultMessage,
            data = null,
        )
    }
}
