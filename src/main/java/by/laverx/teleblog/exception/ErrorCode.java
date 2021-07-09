package by.laverx.teleblog.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    EMAIL_EXISTS(10100),
    INCORRECT_PARAMETER(10101),
    JWT_INVALID(10102),
    ACCESS_DENIED(10103),
    UNAUTHORIZED(10104),
    VERIFICATION_FAILED(10105),
    ENTITY_NOT_FOUND(10106),
    ENTITY_ALREADY_EXISTS(10107);

    private int errorCode;

    ErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
