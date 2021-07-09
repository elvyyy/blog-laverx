package by.laverx.teleblog.locale;

import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@AllArgsConstructor
@Component
public class LocaleTranslator {
    private final MessageSource messageSource;

    public String toLocale(String msgCode) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(msgCode, new Object[0], locale);
    }
}
