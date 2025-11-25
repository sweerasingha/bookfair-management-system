package com.ruh.bms.service;

import org.springframework.scheduling.annotation.Async;

import java.util.Map;


public interface EmailService {


    @Async
    void sendEmail(String to, String subject, String content);

    @Async
    void sendEmailWithAttachment(String to, String subject, String content, String attachmentName, byte[] attachmentData);

    @Async
    void sendReservationConfirmationEmail(String to, String userName, String reservationCode, String eventName, String stallNumber, String qrCodeBase64);

    @Async
    void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables);
}