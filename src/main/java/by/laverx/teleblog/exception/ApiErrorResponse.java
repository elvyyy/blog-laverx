package by.laverx.teleblog.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ApiErrorResponse {
    @JsonProperty(value = "error_code")
    private int errorCode;
    @JsonProperty(value = "error_message")
    private String message;

    public static ApiErrorResponse of(BusinessException businessException) {
        return ApiErrorResponse.builder()
                .errorCode(businessException.getErrorCode())
                .message(businessException.getMessage())
                .build();
    }
}
