package by.laverx.teleblog.exception;

public class EntityNotFoundException extends BusinessException {

    public EntityNotFoundException(String message, int errorCode) {
        super(message, errorCode);
    }
}
