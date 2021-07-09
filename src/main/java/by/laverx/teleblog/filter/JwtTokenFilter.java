package by.laverx.teleblog.filter;

import by.laverx.teleblog.domain.redis.Jwt;
import by.laverx.teleblog.exception.ErrorCode;
import by.laverx.teleblog.exception.JwtValidationException;
import by.laverx.teleblog.service.JwtTokenProvider;
import by.laverx.teleblog.service.JwtWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenProvider jwtTokenProvider;
    private JwtWhitelistService jwtWhitelistService;
    private HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    public void setJwtTokenProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Autowired
    public void setJwtWhitelistService(JwtWhitelistService jwtWhitelistService) {
        this.jwtWhitelistService = jwtWhitelistService;
    }

    @Autowired
    public void setHandlerExceptionResolver(HandlerExceptionResolver handlerExceptionResolver) {
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null) {
            try {
                Jwt jwt = jwtTokenProvider.mapTokenToJwt(token);
                if (jwt != null && !jwtWhitelistService.isInvalidated(jwt)) {
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new JwtValidationException("Json web token is either expired or invalid",
                            ErrorCode.JWT_INVALID.getErrorCode());
                }
            } catch (JwtValidationException jwtValidationException) {
                handlerExceptionResolver.resolveException(request, response, null, jwtValidationException);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
