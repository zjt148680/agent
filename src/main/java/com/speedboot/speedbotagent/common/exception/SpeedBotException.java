package com.speedboot.speedbotagent.common.exception;

public class SpeedBotException extends RuntimeException {
    private final String message;
    private final Throwable cause;

    public SpeedBotException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = cause;
    }

    public SpeedBotException(String message) {
        super(message);
        this.message = message;
        this.cause = null;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }
}
