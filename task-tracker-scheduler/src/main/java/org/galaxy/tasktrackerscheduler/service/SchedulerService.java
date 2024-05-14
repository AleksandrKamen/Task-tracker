package org.galaxy.tasktrackerscheduler.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.galaxy.tasktrackerscheduler.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SchedulerService {

    @Value("${message.report_time_in_hours}")
    Long reportTimeInHours;
    final MessageService messageService;
    final MessageProducerService messageProducerService;
    final UserRepository userRepository;

    @Scheduled(cron = "${message.report_completed_cron}")
    public void createReportCompletedTasks() {
        var allUsersWhoCompletedTaskAfterDate = userRepository.findAllUsersWhoCompletedTaskAfterDate(
                LocalDateTime.now().minusHours(reportTimeInHours));
        allUsersWhoCompletedTaskAfterDate.stream()
                .map(messageService::createCompletedTaskMessage)
                .forEach(messageProducerService::sendMessage);
    }

    @Scheduled(cron = "${message.report_not_completed_cron}")
    public void createReportNotCompletedTasks() {
        userRepository.findAllUsersWhoHaveUnfinishedTasks()
                .stream().map(messageService::createNotCompletedTaskMessage)
                .forEach(messageProducerService::sendMessage);
    }
}
