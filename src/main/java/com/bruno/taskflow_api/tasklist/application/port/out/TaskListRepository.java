package com.bruno.taskflow_api.tasklist.application.port.out;

import com.bruno.taskflow_api.tasklist.domain.model.TaskList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskListRepository {

  TaskList save(TaskList taskList);

  Optional<TaskList> findById(UUID taskListId);

  List<TaskList> findByFilters(UUID workspaceId);

  boolean existsById(UUID taskListId);

  void delete(UUID id);
}
