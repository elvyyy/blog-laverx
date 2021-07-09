package by.laverx.teleblog.listener;

import by.laverx.teleblog.domain.redis.VerificationToken;
import by.laverx.teleblog.dto.UserDto;
import by.laverx.teleblog.service.EmailService;
import by.laverx.teleblog.service.UserService;
import by.laverx.teleblog.service.VerificationTokenService;
import by.laverx.teleblog.service.event.OnRegistrationCompleteEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.UUID;

@Component
@Slf4j
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final UserService userService;
    private final MessageSource messages;
    private final JavaMailSender mailSender;
    private final VerificationTokenService tokenService;
    private final EmailService emailService;


    public RegistrationListener(UserService userService, @Qualifier("messageSource") MessageSource messages,
                                JavaMailSender mailSender, VerificationTokenService tokenService, EmailService emailService) {
        this.userService = userService;
        this.messages = messages;
        this.mailSender = mailSender;
        this.tokenService = tokenService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );
        log.info("Sending email...");
        UserDto user = event.getUser();
        String token = UUID.randomUUID().toString();

        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .timeToLive(1L)
                .userId(user.getId())
                .invalidated(false)
                .build();

        tokenService.save(verificationToken);

        HashMap<String, Object> model = new HashMap<>();
        model.put("name", user.getFirstName());
        model.put("token", token);
        model.put("sign", "Tinderbook");
        model.put("location", "Belarus, Minsk");
        try {
            emailService.sendMessage(user.getEmail(), "Email Confirmation", model);
            log.info("Message sent to {}", user.getEmail());
        } catch (MessagingException e) {
            log.error("Email not sent", e);
        }
    }
}