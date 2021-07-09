package by.laverx.teleblog.exception;

public class EntityExistsException extends BusinessException {

    public EntityExistsException(String message, int errorCode) {
        super(message, errorCode);
    }
}
