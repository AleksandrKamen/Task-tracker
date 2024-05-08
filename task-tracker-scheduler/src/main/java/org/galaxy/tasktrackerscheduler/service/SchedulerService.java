package org.galaxy.tasktrackerscheduler.service;

import lombok.RequiredArgsConstructor;
import org.galaxy.tasktrackerscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    @Value("${message.last_hours}")
    private Long lastHours;

    private final MessageService messageService;
    private final MessageProducerService messageProducerService;
    private final UserRepository userRepository;

    @Scheduled(cron = "${message.report_complited_cron}")
    public void createReportCompletedTasks() {
        var allUsersWhoCompletedTaskAfterDate = userRepository.findAllUsersWhoCompletedTaskAfterDate(
                LocalDateTime.now().minusHours(lastHours));
        allUsersWhoCompletedTaskAfterDate.stream()
                .map(messageService::createCompletedTaskMessage)
                .forEach(messageProducerService::sendMessage);
    }

    @Scheduled(cron = "${message.report_not_complited_cron}")
    public void createReportNotCompletedTasks() {
        userRepository.findAllUsersWhoHaveUnfinishedTasks()
                .stream().map(messageService::createNotCompletedTaskMessage)
                .forEach(messageProducerService::sendMessage);
    }
}
