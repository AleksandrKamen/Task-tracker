package org.galaxy.tasktrackerscheduler.service;

import org.galaxy.tasktrackerscheduler.entity.Task;
import org.galaxy.tasktrackerscheduler.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MessageServiceTest {
    @Autowired
    private MessageService messageService;

    static User user;

    @BeforeEach
    void init() {
        var tasks = List.of(
                Task.builder().title("Задача №1").iscompleted(true).completed_at(LocalDateTime.now().minusHours(2L)).build(),
                Task.builder().title("Задача №2").iscompleted(true).completed_at(LocalDateTime.now().minusHours(10L)).build(),
                Task.builder().title("Задача №3").iscompleted(true).completed_at(LocalDateTime.now().minusHours(30L)).build(),
                Task.builder().title("Задача №4").iscompleted(false).build(),
                Task.builder().title("Задача №5").iscompleted(false).build(),
                Task.builder().title("Задача №6").iscompleted(false).build(),
                Task.builder().title("Задача №7").iscompleted(false).build(),
                Task.builder().title("Задача №8").iscompleted(false).build(),
                Task.builder().title("Задача №9").iscompleted(false).build()
        );

        user = User.builder()
                .username("test@mail.com")
                .tasks(tasks)
                .build();
    }

    @Test
    void createCompletedTaskMessage_ReturnsMessageWith2Tasks() {
        var actualResult = messageService.createCompletedTaskMessage(user);
        assertEquals(actualResult.getEmail(), "test@mail.com");
        assertEquals(actualResult.getTitle(), "За последние сутки выполнено задач: 2");
        assertEquals(actualResult.getText(), "Задача №1 Задача №2");
    }

    @Test
    void createNotCompletedTaskMessage_ReturnsMessageWith3Tasks() {
        var actualResult = messageService.createNotCompletedTaskMessage(user);
        assertEquals(actualResult.getEmail(), "test@mail.com");
        assertEquals(actualResult.getTitle(), "Осталось не завершенных задач: 6");
        assertEquals(actualResult.getText(), "Задача №4 Задача №5 Задача №6 Задача №7 Задача №8");
    }
}