package org.galaxy.tasktrackerscheduler.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galaxy.tasktrackerscheduler.dto.MessageDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageProducerService {
    @Value("${rabbit.queue_name}")
    private String queueName;
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(MessageDto messageDto) {
        rabbitTemplate.convertAndSend(queueName, messageDto);
        log.info("Message {} was send to user {}", messageDto.getTitle(), messageDto.getEmail());
    }
}
