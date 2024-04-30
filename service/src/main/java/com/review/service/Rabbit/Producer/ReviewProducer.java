package com.review.service.Rabbit.Producer;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewProducer {
    private final RabbitTemplate rabbitTemplate;

    public void createMessage(int partyId){
        rabbitTemplate.convertAndSend("reviewQueue", partyId);
    }
}
