package org.galaxy.tasktrackerscheduler.service;

import org.galaxy.tasktrackerscheduler.dto.MessageDto;
import org.galaxy.tasktrackerscheduler.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class MessageService {
    @Value("${message.last_hours}")
    private Long lastHours;
    @Value("${message.text_count_limit}")
    private Long textCountLimit;


    private final String COMPLETED_TASKS_LAST_DAY = "За последние сутки выполнено задач: %d";
    private final String NOT_COMPLETED_TASKS = "Осталось не завершенных задач: %d";


    public MessageDto createCompletedTaskMessage(User user) {
        var allCompletedTask = user.getTasks().stream()
                .filter(task -> task.getIscomplited())
                .filter(task -> task.getCompleted_at().isAfter(LocalDateTime.now().minusHours(lastHours)))
                .limit(textCountLimit)
                .toList();
        var allCompletedTaskTitles = allCompletedTask
                .stream()
                .map(task -> task.getTitle())
                .collect(Collectors.joining(" "));
        return createMessageDto(user.getUsername(), COMPLETED_TASKS_LAST_DAY.formatted(allCompletedTask.size()), allCompletedTaskTitles);
    }

    public MessageDto createNotCompletedTaskMessage(User user) {
        var allNotCompletedTask = user.getTasks()
                .stream()
                .filter(task -> !task.getIscomplited())
                .toList();
        var allNotCompletedTaskTitles = allNotCompletedTask
                .stream()
                .map(task -> task.getTitle())
                .limit(textCountLimit)
                .collect(Collectors.joining(" "));
        return createMessageDto(user.getUsername(), NOT_COMPLETED_TASKS.formatted(allNotCompletedTask.size()), allNotCompletedTaskTitles);
    }

    private MessageDto createMessageDto(String email, String title, String text) {
        return MessageDto.builder()
                .email(email)
                .title(title)
                .text(text)
                .build();
    }

}
