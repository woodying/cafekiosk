package com.example.cafekiosk.spring.api.service.order

import com.example.cafekiosk.spring.api.service.mail.MailService
import com.example.cafekiosk.spring.domain.order.Order
import com.example.cafekiosk.spring.domain.order.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class OrderStatisticsService(
    private val orderRepository: OrderRepository,
    private val mailService: MailService
) {

    @Transactional
    fun sendOrderStatisticsMail(email: String, orderDate: LocalDate): Boolean {
        val orders = orderRepository.findByRegisteredDateTimeBetweenAndStatus(
            orderDate.atStartOfDay(),
            orderDate.plusDays(1).atStartOfDay(),
            Order.Status.PAYMENT_COMPLETED
        )

        val totalAmount = orders.sumOf { it.totalPrice }

        val result = mailService.send(
            senderEmail = "no-reply@test.com",
            recipientEmail = email,
            title = "[매출 통계] $orderDate",
            content = "총 매출 합계는 ${totalAmount}원 입니다."
        )

        if (!result) {
            throw IllegalArgumentException("매출 통계 메일 전송에 실패했습니다.")
        }

        return true
    }
}
