package org.galaxy.tasktrackeremailsender.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.galaxy.tasktrackeremailsender.dto.MessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageSenderService {
    @Value("${spring.mail.emailFrom}")
    String emailFrom;
    final JavaMailSender javaMailSender;

    public void sendMessage(MessageDto messageDto) {
        var newMessage = new SimpleMailMessage();
        newMessage.setFrom(emailFrom);
        newMessage.setTo(messageDto.getEmail());
        newMessage.setSubject(messageDto.getTitle());
        newMessage.setText(messageDto.getText());
        javaMailSender.send(newMessage);
    }
}
