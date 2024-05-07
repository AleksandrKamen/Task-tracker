package org.galaxy.tasktrackeremailsender.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.galaxy.tasktrackeremailsender.dto.MessageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsumerService {

    MessageSenderService messageSenderService;

    @RabbitListener(queues = "${rabbit.queue_name}")
    public void consumeAndSendEmail(MessageDto messageDto) {
        messageSenderService.sendMessage(messageDto);
    }
}
