package com.alekgor.task.common;

import com.alekgor.task.common.exception.NotFoundResourceException;
import com.alekgor.task.controller.dto.ErrorMessage;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;

/**
 * Глобальный handler ошибок, прокидывает ошибки с дополнительной информацией.
 */
@RestControllerAdvice
public class QuoteControllerExceptionHandler {

    @ExceptionHandler(value = {ValidationException.class, ConstraintViolationException.class})
    public ResponseEntity<?> validationException(ValidationException ex) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorMessage(badRequest, "Validation error: " + ex.getMessage()), badRequest);
    }


    @ExceptionHandler(value = {NotFoundResourceException.class})
    public ResponseEntity<?> resourceNotFoundException(NotFoundResourceException ex) {
        HttpStatus badRequest = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(new ErrorMessage(badRequest, "Not found error: " + ex.getMessage()), badRequest);
    }
}
