package com.siteminder.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service 
public class EmailServiceFactory {

    private static final Logger logger = LoggerFactory.getLogger(EmailServiceFactory.class);

    @Autowired
    private MailgunEmailService mailgunEmailService;

    @Autowired
    private SendGridEmailService sendGridEmailService; 

    @Autowired
    private MailersendEmailService mailersendEmailService;

    public boolean sendEmail(String subject, String body, List<String> recipients, List<String> ccRecipients, List<String> bccRecipients) {
        EmailService[] emailServices = { mailgunEmailService, sendGridEmailService, mailersendEmailService };
        RuntimeException lastException = null;

        logger.info("Starting email sending process to recipients: {}", recipients);
        for (EmailService emailService : emailServices) {
            try {
                logger.info("Attempting to send email using {}.", emailService.getClass().getSimpleName());
                boolean isSuccess = emailService.sendEmail(subject, body, recipients, ccRecipients, bccRecipients);
                if (isSuccess) {
                    logger.info("Email sent successfully using {}.", emailService.getClass().getSimpleName());
                    return true; // Exit if email is sent successfully
                }
            } catch (RuntimeException e) {
                lastException = e; // Keep track of the last exception
                logger.error("Failed to send email using {}: {}", emailService.getClass().getSimpleName(), e.getMessage());
            }
        }

        // If all fail, log and throw the last encountered exception
        logger.error("Failed to send email using all providers.");
        throw new RuntimeException("Failed to send email using all providers.", lastException);
    }
    
    
}