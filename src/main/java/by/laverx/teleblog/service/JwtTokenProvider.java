package by.laverx.teleblog.service;

import by.laverx.teleblog.domain.mysql.Role;
import by.laverx.teleblog.domain.redis.Jwt;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface JwtTokenProvider {
    String generateToken(final String subject, Collection<? extends Role> role);

    Authentication getAuthentication(String token);

    String resolveToken(HttpServletRequest request);

    Jwt mapTokenToJwt(String token);

    boolean isExpired(String token);
}
