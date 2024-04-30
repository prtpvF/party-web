package com.review.service.ExceptionsHandler;

import com.review.service.Exceptions.ExceptionMapper;
import com.review.service.Exceptions.PartyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@ControllerAdvice
@RequiredArgsConstructor
public class Handler {
    private final RabbitTemplate rabbitTemplate;
    private static final Logger LOGGER   = LoggerFactory.getLogger(Handler.class);

    @ExceptionHandler(value = PartyNotFoundException.class)
    public void partyNotFoundHandler(PartyNotFoundException e){
        HttpStatus status = HttpStatus.NOT_FOUND;
        ExceptionMapper mapper = new ExceptionMapper(e.getMessage(), e,status, ZonedDateTime.now(ZoneId.of("Z")));
        LOGGER.error("party not found" + mapper.toString());
    }
}
