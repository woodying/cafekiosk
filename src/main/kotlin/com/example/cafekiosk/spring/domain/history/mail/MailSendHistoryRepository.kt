package com.example.cafekiosk.spring.domain.history.mail

import org.springframework.data.jpa.repository.JpaRepository

interface MailSendHistoryRepository : JpaRepository<MailSendHistory, Long>
