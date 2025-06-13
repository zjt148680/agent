package com.speedboot.speedbotagent.common.exception;

public class SpeedBotErrorResponse {
    private static final int FAIL_CODE = 500;

    private final int code;
    private final String message;

    public SpeedBotErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static  SpeedBotErrorResponse fail(String message) {
        return new SpeedBotErrorResponse(FAIL_CODE, message);
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
