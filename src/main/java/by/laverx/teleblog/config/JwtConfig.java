package by.laverx.teleblog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class JwtConfig {
    @Value("${security.jwt.secret}")
    private String secretKey;
    @Value("${security.jwt.ttl}")
    private long timeToLive;
}
