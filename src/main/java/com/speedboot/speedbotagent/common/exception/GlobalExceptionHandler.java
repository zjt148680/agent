package com.speedboot.speedbotagent.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// todo i18n
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(SpeedBotException.class)
    public ResponseEntity<SpeedBotErrorResponse> handleSpeedBotException(SpeedBotException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return getErrorResp("系统内部错误");
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<SpeedBotErrorResponse> handleDataAccessException(DataAccessException ex) {
        LOGGER.error("数据库访问异常。", ex);
        return getErrorResp("数据库访问异常");
    }

    private ResponseEntity<SpeedBotErrorResponse> getErrorResp(String failMessage) {
        SpeedBotErrorResponse error = SpeedBotErrorResponse.fail(failMessage);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
