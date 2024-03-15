package com.ratz.greenbites.messaging;

import com.ratz.greenbites.email.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailConsumer {

    private final EmailService emailService;

    @RabbitListener(queues = "emailQueue")
    public void consumeMessage(String email) {
        emailService.sendSimpleMessage(email, "Welcome to GreenBites", "Welcome to GreenBites, we are happy to have you as a customer");
    }

}
