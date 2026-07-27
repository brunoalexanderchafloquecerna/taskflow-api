package com.bruno.taskflow_api.tasklist.application.service;

import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import com.bruno.taskflow_api.tasklist.application.exception.TaskListNotFoundException;
import com.bruno.taskflow_api.tasklist.application.port.in.TaskListUseCase;
import com.bruno.taskflow_api.tasklist.application.port.out.TaskListRepository;
import com.bruno.taskflow_api.tasklist.domain.model.TaskList;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskListService implements TaskListUseCase {

  private final TaskListRepository taskListRepository;

  private final AuthenticatedUserProvider authenticatedUserProvider;

  public TaskListService(TaskListRepository taskListRepository,
      AuthenticatedUserProvider authenticatedUserProvider) {
    this.taskListRepository = taskListRepository;
    this.authenticatedUserProvider = authenticatedUserProvider;
  }

  @Override
  @Transactional
  public TaskList createTaskList(String name, UUID workspaceId, int position) {
    return taskListRepository.save(
        TaskList.create(name, workspaceId, authenticatedUserProvider.getCurrentUserId(), position));
  }

  @Override
  @Transactional(readOnly = true)
  public TaskList getTaskListById(UUID id) {
    return taskListRepository.findById(id).orElseThrow(() -> new TaskListNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<TaskList> getTaskListsByFilters(UUID workspaceId) {
    return taskListRepository.findByFilters(workspaceId);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsTaskListById(UUID id) {
    return taskListRepository.existsById(id);
  }

  @Override
  @Transactional
  public TaskList updateTaskListById(UUID id, String name, UUID workspaceId, int position) {
    TaskList taskList = taskListRepository.findById(id)
        .orElseThrow(() -> new TaskListNotFoundException(id));
    taskList.updateInformation(name, workspaceId, position);
    return taskListRepository.save(taskList);
  }

  @Override
  @Transactional
  public void deleteTaskListById(UUID id) {
    taskListRepository.delete(id);
  }
}
