package com.example.cafekiosk.spring.api.service.mail

import com.example.cafekiosk.spring.client.MailSendClient
import com.example.cafekiosk.spring.domain.history.mail.MailSendHistory
import com.example.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class MailServiceTest {

    private val mailSendClient: MailSendClient = mockk()
    private val mailSendHistoryRepository: MailSendHistoryRepository = mockk()

    private val sut = MailService(mailSendClient, mailSendHistoryRepository)

    @Test
    fun `메일 전송 테스트`() {
        // given
        every {
            mailSendClient.send(any(), any(), any(), any())
        } returns true

        every {
            mailSendHistoryRepository.save(any())
        } returns MailSendHistory(
            id = 0L,
            senderEmail = "sender@test.com",
            recipientEmail = "test@test.com",
            title = "Test Title",
            content = "Test Content"
        )

        // when
        val result = sut.send(
            senderEmail = "sender@test.com",
            recipientEmail = "test@test.com",
            title = "Test Title",
            content = "Test Content"
        )

        // then
        verify { mailSendClient.send(any(), any(), any(), any()) }
        verify { mailSendHistoryRepository.save(any()) }
        assertTrue(result)
    }
}
