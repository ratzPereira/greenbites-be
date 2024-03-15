package com.ratz.greenbites.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {


    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;

    public void sendEmailRequest(String email) {
        rabbitTemplate.convertAndSend(exchange.getName(), "emailRoutingKey", email);
    }
}
