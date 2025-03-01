package com.example.cafekiosk.spring.api

import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ApiControllerAdvice {

    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): ApiResponse<*> {
        return ApiResponse(
            code = 400,
            status = HttpStatus.BAD_REQUEST,
            message = e.allErrors.joinToString { it.defaultMessage ?: "" },
            data = null,
        )
    }
}
