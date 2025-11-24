package com.ruh.bms.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
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

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Value("${app.mail.from}")
    private String fromEmail;

    /**
     * Sends an email asynchronously
     * @param to Recipient email address
     * @param subject Email subject
     * @param content Email content (HTML)
     */
    @Async
    @Override
    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email to: {}", to, e);
        }
    }

    /**
     * Sends an email with attachment asynchronously
     * @param to Recipient email address
     * @param subject Email subject
     * @param content Email content (HTML)
     * @param attachmentName Attachment file name
     * @param attachmentData Attachment data as byte array
     */
    @Async
    @Override
    public void sendEmailWithAttachment(String to, String subject, String content,
                                        String attachmentName, byte[] attachmentData) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            if (attachmentData != null && attachmentName != null) {
                helper.addAttachment(attachmentName, new ByteArrayResource(attachmentData));
            }

            mailSender.send(message);
            log.info("Email with attachment sent successfully to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send email with attachment to: {}", to, e);
        }
    }

    /**
     * Sends a reservation confirmation email with QR code
     * @param to Recipient email address
     * @param userName User's full name
     * @param reservationCode Reservation code
     * @param eventName Event name
     * @param stallNumber Stall number
     * @param qrCodeBase64 Base64 encoded QR code image
     */
    @Async
    @Override
    public void sendReservationConfirmationEmail(String to, String userName, String reservationCode,
                                                 String eventName, String stallNumber, String qrCodeBase64) {
        try {
            Context context = new Context();
            context.setVariable("userName", userName);
            context.setVariable("reservationCode", reservationCode);
            context.setVariable("eventName", eventName);
            context.setVariable("stallNumber", stallNumber);
            context.setVariable("qrCode", qrCodeBase64);

            String htmlContent = templateEngine.process("reservation-confirmation", context);

            sendEmail(to, "Reservation Confirmation - " + eventName, htmlContent);
        } catch (Exception e) {
            log.error("Failed to send reservation confirmation email to: {}", to, e);
        }
    }

    /**
     * Sends a generic email using Thymeleaf template
     * @param to Recipient email address
     * @param subject Email subject
     * @param templateName Thymeleaf template name
     * @param variables Template variables
     */
    @Async
    @Override
    public void sendTemplateEmail(String to, String subject, String templateName, Map<String, Object> variables) {
        try {
            Context context = new Context();
            context.setVariables(variables);

            String htmlContent = templateEngine.process(templateName, context);
            sendEmail(to, subject, htmlContent);
        } catch (Exception e) {
            log.error("Failed to send template email to: {}", to, e);
        }
    }
}
