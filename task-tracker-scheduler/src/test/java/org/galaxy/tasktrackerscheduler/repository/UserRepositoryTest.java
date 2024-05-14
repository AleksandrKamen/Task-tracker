package org.galaxy.tasktrackerscheduler.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@Transactional
@ActiveProfiles("dev")
@Sql("/sql/init.sql")
@Sql("/sql/data.sql")
class UserRepositoryTest {
    @Value("${message.report_time_in_hours}")
    private Long reportTimeInHours;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres");

    @Autowired
    UserRepository userRepository;

    @Test
    void findAllUsersWhoHaveUnfinishedTasks_Returns_3_Users() {
        var actualResult = userRepository.findAllUsersWhoHaveUnfinishedTasks();
        assertThat(actualResult).hasSize(3);
    }
    @Test
    void findAllUsersWhoCompletedTaskLastDay_Returns_2_Users() {
        var actualResult = userRepository.findAllUsersWhoCompletedTaskAfterDate(LocalDateTime.now().minusHours(reportTimeInHours));
        assertThat(actualResult).hasSize(2);
    }
}