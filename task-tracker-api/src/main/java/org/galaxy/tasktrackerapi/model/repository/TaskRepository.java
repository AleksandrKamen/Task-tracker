package org.galaxy.tasktrackerapi.model.repository;

import org.galaxy.tasktrackerapi.model.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findAllByIscomplited(Boolean iscomplited);
}
