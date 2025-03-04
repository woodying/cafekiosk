package com.example.cafekiosk.spring.api.service.mail

import com.example.cafekiosk.spring.client.MailSendClient
import com.example.cafekiosk.spring.domain.history.mail.MailSendHistory
import com.example.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSendClient: MailSendClient,
    private val mailSendHistoryRepository: MailSendHistoryRepository
) {
    fun send(senderEmail: String, recipientEmail: String, title: String, content: String): Boolean {
        val result =  mailSendClient.send(
            senderEmail = senderEmail,
            recipientEmail = recipientEmail,
            title = title,
            content = content
        )

        if (result) {
            mailSendHistoryRepository.save(
                MailSendHistory(
                    senderEmail = senderEmail,
                    recipientEmail = recipientEmail,
                    title = title,
                    content = content
                )
            )

            return true
        }

        return false
    }
}
