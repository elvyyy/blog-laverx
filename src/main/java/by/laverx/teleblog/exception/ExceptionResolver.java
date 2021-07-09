package by.laverx.teleblog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionResolver {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({EntityExistsException.class})
    public ApiErrorResponse resolveGenericException(EntityExistsException exception) {
        return ApiErrorResponse.of(exception);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ApiErrorResponse resultIncorrectInputParameterException(ConstraintViolationException exception) {
        return ApiErrorResponse.of(new IncorrectInputParameter(exception.getLocalizedMessage(),
                ErrorCode.INCORRECT_PARAMETER.getErrorCode()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiErrorResponse resultIncorrectInputParameterException(MethodArgumentNotValidException e) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(error -> errorMessageBuilder
                .append(error.getDefaultMessage())
                .append("; "));

        ApiErrorResponse apiExceptionInfo = ApiErrorResponse.builder()
                .message(errorMessageBuilder.toString())
                .errorCode(ErrorCode.INCORRECT_PARAMETER.getErrorCode())
                .build();
        return apiExceptionInfo;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IdentityVerificationException.class)
    public ApiErrorResponse resultIncorrectInputParameterException(IdentityVerificationException e) {
        return ApiErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .message(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ApiErrorResponse resultIncorrectInputParameterException(EntityNotFoundException e) {
        return ApiErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .message(e.getLocalizedMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JwtValidationException.class)
    public ApiErrorResponse resultIncorrectInputParameterException(JwtValidationException e) {
        return ApiErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .message(e.getLocalizedMessage())
                .build();
    }
}
