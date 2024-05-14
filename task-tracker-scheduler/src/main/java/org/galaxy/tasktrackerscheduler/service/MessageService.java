package org.galaxy.tasktrackerscheduler.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.galaxy.tasktrackerscheduler.dto.MessageDto;
import org.galaxy.tasktrackerscheduler.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageService {
    @Value("${message.report_time_in_hours}")
    Long reportTimeInHours;
    @Value("${message.titles_count_limit}")
    Long messageTitlesLimit;
    final MessageSource messageSource;

    public MessageDto createCompletedTaskMessage(User user) {
        var allCompletedTask = user.getTasks().stream()
                .filter(task -> task.getIscompleted())
                .filter(task -> task.getCompleted_at().isAfter(LocalDateTime.now().minusHours(reportTimeInHours)))
                .toList();
        var allCompletedTaskTitles = allCompletedTask
                .stream()
                .map(task -> task.getTitle())
                .limit(messageTitlesLimit)
                .collect(Collectors.joining(" "));
        return createMessageDto(
                user.getUsername(),
                messageSource.getMessage("messages.report.title.completed_task", new Object[]{allCompletedTask.size()}, LocaleContextHolder.getLocale()),
                allCompletedTaskTitles);
    }

    public MessageDto createNotCompletedTaskMessage(User user) {
        var allNotCompletedTask = user.getTasks()
                .stream()
                .filter(task -> !task.getIscompleted())
                .toList();
        var allNotCompletedTaskTitles = allNotCompletedTask
                .stream()
                .map(task -> task.getTitle())
                .limit(messageTitlesLimit)
                .collect(Collectors.joining(" "));
        return createMessageDto(
                user.getUsername(),
                messageSource.getMessage("messages.report.title.not_completed_task", new Object[]{allNotCompletedTask.size()}, LocaleContextHolder.getLocale()),
                allNotCompletedTaskTitles);
    }

    private MessageDto createMessageDto(String email, String title, String text) {
        return MessageDto.builder()
                .email(email)
                .title(title)
                .text(text)
                .build();
    }

}
