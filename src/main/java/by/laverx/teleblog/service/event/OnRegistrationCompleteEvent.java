package by.laverx.teleblog.service.event;

import by.laverx.teleblog.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private final UserDto user;
    private final Locale locale;
    private final String appUrl;


    public OnRegistrationCompleteEvent(UserDto user, Locale locale, String appUrl) {
        super(user);

        this.user = user;
        this.locale = locale;
        this.appUrl = appUrl;
    }
}
