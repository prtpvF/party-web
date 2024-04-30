package com.review.service.Rabbit.Consumer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewConsumer {

    @RabbitListener(queues = "reviewQueue")
    public boolean isPartyExists(){
        boolean isExists = false;
        return isExists;
    }
}
