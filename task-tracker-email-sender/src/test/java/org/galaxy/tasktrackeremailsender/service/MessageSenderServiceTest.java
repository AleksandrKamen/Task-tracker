package org.galaxy.tasktrackeremailsender.service;

import org.galaxy.tasktrackeremailsender.dto.MessageDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageSenderServiceTest {
    @Mock
    JavaMailSender mailSender;

    @InjectMocks
    MessageSenderService messageSenderService;

    @Test
    void sendMessage_messageWasSend(){
        var newMessage = MessageDto.builder()
                .email("test@mail")
                .title("Test title")
                .text("Test text")
                .build();
        messageSenderService.sendMessage(newMessage);
        verify(mailSender).send(any(SimpleMailMessage.class));
        verifyNoMoreInteractions(mailSender);
    }

}