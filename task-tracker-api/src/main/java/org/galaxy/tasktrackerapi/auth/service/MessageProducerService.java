package org.galaxy.tasktrackerapi.auth.service;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerapi.auth.dto.MessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducerService {
    @Value("${rabbit.queue_name}")
    private String queueName;
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(MessageDto messageDto) {
        rabbitTemplate.convertAndSend(queueName, messageDto);
    }
}
