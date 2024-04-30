package com.review.service.Exceptions;

import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public class ExceptionMapper extends Exception{
    private final String message;
    private final String exceptionType;
    private final HttpStatus httpStatus;
    private final ZonedDateTime timestamp;

    public ExceptionMapper(String message, Throwable throwable, HttpStatus httpStatus, ZonedDateTime timestamp) {
        this.message = message;
        this.exceptionType = throwable.getClass().getName();
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }



    public String getMessage() {
        return message;
    }

    public String getExceptionType() {
        return exceptionType;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ZonedDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "ExceptionMapper{" +
                "message='" + message + '\'' +
                ", exceptionType='" + exceptionType + '\'' +
                ", httpStatus=" + httpStatus +
                ", timestamp=" + timestamp +
                '}';
    }
}