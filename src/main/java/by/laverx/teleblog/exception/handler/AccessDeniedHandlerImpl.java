package by.laverx.teleblog.exception.handler;

import by.laverx.teleblog.exception.ApiErrorResponse;
import by.laverx.teleblog.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        PrintWriter writer = response.getWriter();
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .message("Forbidden")
                .errorCode(ErrorCode.ACCESS_DENIED.getErrorCode())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String responseJson = objectMapper.writeValueAsString(apiErrorResponse);
        writer.print(responseJson);
    }

}
