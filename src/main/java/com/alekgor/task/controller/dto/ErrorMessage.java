package com.alekgor.task.controller.dto;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.Date;

@ToString
@Getter
public class ErrorMessage {
    private final Date timestamp;
    private final HttpStatus status;
    private final String error;

    public ErrorMessage(HttpStatus status, String error) {
        this.timestamp = new Date();
        this.status = status;
        this.error = error;
    }
}
