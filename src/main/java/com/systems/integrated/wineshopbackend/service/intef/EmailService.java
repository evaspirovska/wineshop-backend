package com.systems.integrated.wineshopbackend.service.intef;

import javax.mail.MessagingException;

public interface EmailService {
    void sendEmail(String subject, String recipient, String content) throws MessagingException;
}
