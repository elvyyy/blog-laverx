package by.laverx.teleblog.exception;

public class JwtValidationException extends BusinessException {
    public JwtValidationException(String message, int errorCode) {
        super(message, errorCode);
    }
}
