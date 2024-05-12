package org.galaxy.tasktrackerscheduler.repository;

import org.galaxy.tasktrackerscheduler.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u from User u LEFT JOIN u.tasks t WHERE t.iscompleted = false")
    List<User> findAllUsersWhoHaveUnfinishedTasks();

    @Query("SELECT u from User u LEFT JOIN u.tasks t WHERE t.completed_at >:date")
    List<User> findAllUsersWhoCompletedTaskAfterDate(LocalDateTime date);
}
