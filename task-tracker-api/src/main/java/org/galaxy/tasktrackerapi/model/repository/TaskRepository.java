package org.galaxy.tasktrackerapi.model.repository;

import org.galaxy.tasktrackerapi.model.entity.Task;
import org.galaxy.tasktrackerapi.model.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByUser(User user);
    List<Task> findByIscomplitedAndUser(Boolean iscomplited, User user);
    Optional<Task> findByIdAndUser(Long id, User user);
    Optional<Task> findByTitleAndUser(String title, User user);
    List<Task> findByTitleLikeAndUser(String title, User user);

}
