package by.laverx.teleblog.service.impl;

import by.laverx.teleblog.config.JwtConfig;
import by.laverx.teleblog.domain.mysql.Role;
import by.laverx.teleblog.domain.redis.Jwt;
import by.laverx.teleblog.exception.ErrorCode;
import by.laverx.teleblog.exception.JwtValidationException;
import by.laverx.teleblog.service.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.lang.System.currentTimeMillis;

@Service
@AllArgsConstructor
public class JwtTokenProviderImpl implements JwtTokenProvider {
    public static final String TOKEN_PREAMBLE = "Bearer ";
    public static final String AUTH_HEADER = HttpHeaders.AUTHORIZATION;
    public static final String ROLE_CLAIM = "Role";

    private final JwtConfig jwtConfig;
    private final UserDetailsService userDetailsService;

    @PostConstruct
    public void init() {
        byte[] bytes = jwtConfig.getSecretKey().getBytes();
        String encodedSecretKey = Base64.getEncoder().encodeToString(bytes);
        jwtConfig.setSecretKey(encodedSecretKey);
    }

    @Override
    public String generateToken(String subject, Collection<? extends Role> roles) {
        Instant issuedAt = Instant.now();
        Instant expiration = issuedAt.plusMillis(jwtConfig.getTimeToLive());
        List<String> roleNames = roles.stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        return Jwts.builder()
                .setId(UUID.randomUUID().toString())
                .setSubject(subject)
                .claim(ROLE_CLAIM, roleNames)
                .setIssuedAt(Date.from(issuedAt))
                .setExpiration(Date.from(expiration))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecretKey())
                .compact();
    }

    public Jwt mapTokenToJwt(String token) {
        Claims body = retrieveBody(token);
        long timeToLive = body.getExpiration().getTime() - currentTimeMillis();
        return Jwt.builder()
                .id(body.getId())
                .subject(body.getSubject())
                .timeToLive(timeToLive)
                .build();
    }

    private Claims retrieveBody(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtConfig.getSecretKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtValidationException("Invalid token", ErrorCode.JWT_INVALID.getErrorCode());
        }
    }

    @Override
    public Authentication getAuthentication(String token) {
        String subject = retrieveBody(token)
                .getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(subject);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (StringUtils.hasText(authHeader) && authHeader.startsWith(TOKEN_PREAMBLE)) {
            return authHeader.substring(TOKEN_PREAMBLE.length());
        }
        return null;
    }

    @Override
    public boolean isExpired(String token) {
        return retrieveBody(token)
                .getExpiration()
                .before(new Date());
    }
}
