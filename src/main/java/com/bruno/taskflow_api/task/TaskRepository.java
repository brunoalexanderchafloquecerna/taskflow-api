package com.bruno.taskflow_api.task;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, UUID> {

  List<Task> findByStatus(TaskStatusEnum status);

  List<Task> findByTaskListId(UUID taskListId);

  List<Task> findByStatusAndTaskListId(TaskStatusEnum status, UUID taskListId);

  long countByStatus(TaskStatusEnum status);
}
