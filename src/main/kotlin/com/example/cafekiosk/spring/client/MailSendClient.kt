package com.example.cafekiosk.spring.client

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MailSendClient {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun send(senderEmail: String, recipientEmail: String, title: String, content: String): Boolean {
        logger.info(
            "메일 전송 - " +
                    "senderEmail: $senderEmail, " +
                    "recipientEmail: $recipientEmail, " +
                    "title: $title, " +
                    "content: $content"
        )
        return true
    }
}
