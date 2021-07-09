package by.laverx.teleblog.exception;

public class IncorrectInputParameter extends BusinessException{
    public IncorrectInputParameter(String message, int errorCode) {
        super(message, errorCode);
    }
}
