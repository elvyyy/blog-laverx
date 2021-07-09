package by.laverx.teleblog.service;

import javax.mail.MessagingException;

import java.util.Map;

public interface EmailService {
    void sendMessage(String to, String subject, Map<String, Object> templateModel) throws MessagingException;
}
