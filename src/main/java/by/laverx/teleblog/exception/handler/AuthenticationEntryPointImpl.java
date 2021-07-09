package by.laverx.teleblog.exception.handler;

import by.laverx.teleblog.exception.ApiErrorResponse;
import by.laverx.teleblog.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        PrintWriter writer = response.getWriter();
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .errorCode(ErrorCode.UNAUTHORIZED.getErrorCode())
                .message("Unauthorized")
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(apiErrorResponse);
        writer.print(jsonResponse);
    }

}
