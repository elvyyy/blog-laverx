package by.laverx.teleblog.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private int errorCode;

    public BusinessException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
