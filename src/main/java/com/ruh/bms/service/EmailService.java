package com.ruh.bms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Map;


public interface EmailService {


    @Async
    void sendEmail(String to, String subject, String content);

    @Async
    void sendEmailWithAttachment(String to, String subject, String content,
                                 String attachmentName, byte[] attachmentData);

    @Async
    void sendReservationConfirmationEmail(String to, String userName, String reservationCode,
                                          String eventName, String stallNumber, String qrCodeBase64);

    @Async
    void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables);
}