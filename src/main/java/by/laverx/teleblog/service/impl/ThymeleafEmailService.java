package by.laverx.teleblog.service.impl;

import by.laverx.teleblog.service.EmailService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Map;

@AllArgsConstructor
@Service
@Slf4j
public class ThymeleafEmailService implements EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine thymeleafTemplateEngine;
    private final TaskExecutor taskExecutor;

    @Override
    public void sendMessage(String to, String subject, Map<String, Object> templateModel) {
        taskExecutor.execute(() -> {
            try {
                Context thymeleafContext = new Context();
                thymeleafContext.setVariables(templateModel);
                String htmlBody = thymeleafTemplateEngine.process("confirm-email-template.html", thymeleafContext);
                sendHtmlMail(to, subject, htmlBody);
            } catch (MessagingException e) {
                log.error("Cannot send email to {}", to, e);
            }
        });
    }

    private void sendHtmlMail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom("tagirovicv@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true);
        mailSender.send(message);
    }
}
