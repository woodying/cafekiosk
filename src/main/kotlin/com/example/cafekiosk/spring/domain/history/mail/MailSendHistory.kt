package com.example.cafekiosk.spring.domain.history.mail

import com.example.cafekiosk.spring.domain.BaseEntity
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class MailSendHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val senderEmail: String,
    val recipientEmail: String,
    val title: String,
    val content: String
) : BaseEntity()
