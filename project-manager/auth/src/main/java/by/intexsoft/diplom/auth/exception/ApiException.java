package by.intexsoft.diplom.auth.exception;

import org.springframework.http.HttpStatus;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ApiException {
    private  RuntimeException exception;
    private final HttpStatus status;

    public ApiException(RuntimeException exception, HttpStatus status) {
        this.exception = exception;
        this.status = status;
    }


    public String getMessage() {
        return exception.getMessage();
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

    public ZonedDateTime getTimestamp() {
        return ZonedDateTime.now(ZoneId.of("Z"));
    }
}