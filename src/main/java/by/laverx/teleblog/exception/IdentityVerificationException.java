package by.laverx.teleblog.exception;

public class IdentityVerificationException extends BusinessException {

    public IdentityVerificationException(String message, int errorCode) {
        super(message, errorCode);
    }
}
