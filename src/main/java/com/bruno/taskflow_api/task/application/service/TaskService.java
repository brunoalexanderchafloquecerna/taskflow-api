package com.bruno.taskflow_api.task.application.service;

import com.bruno.taskflow_api.task.application.exception.TaskListNotFoundException;
import com.bruno.taskflow_api.task.application.exception.TaskNotFoundException;
import com.bruno.taskflow_api.task.application.port.in.TaskUseCase;
import com.bruno.taskflow_api.task.application.port.out.TaskListGateway;
import com.bruno.taskflow_api.task.application.port.out.TaskRepository;
import com.bruno.taskflow_api.task.domain.model.Task;
import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService implements TaskUseCase {

  private final TaskRepository taskRepository;

  private final TaskListGateway taskListGateway;

  public TaskService(TaskRepository taskRepository, TaskListGateway taskListGateway) {
    this.taskRepository = taskRepository;
    this.taskListGateway = taskListGateway;
  }

  @Override
  @Transactional
  public Task createTask(String title, String description, UUID taskListId) {
    this.checkIfTaskListExists(taskListId);
    Task task = Task.create(title, description, taskListId);
    return taskRepository.save(task);
  }

  @Override
  @Transactional
  public Task changeTaskStatus(UUID id, TaskStatus status) {
    Task taskFound = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    taskFound.moveTo(status);
    return taskRepository.save(taskFound);
  }

  @Override
  @Transactional
  public Task updateTaskInformation(UUID id, String title, String description, TaskStatus status,
      UUID taskListId) {
    Task taskFound = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    checkIfTaskListExists(taskListId);
    taskFound.updateInformation(title, description, status, taskListId);
    return taskRepository.save(taskFound);
  }

  @Override
  @Transactional(readOnly = true)
  public Task getTaskById(UUID id) {
    return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Task> getTasksByFilters(UUID taskListId, TaskStatus status) {
    return taskRepository.findTasksByFilters(taskListId, status);
  }

  @Override
  @Transactional
  public void deleteTask(UUID id) {
    taskRepository.deleteById(id);
  }

  @Override
  @Transactional
  public void deleteTasksByTaskListId(UUID taskListId) {
    checkIfTaskListExists(taskListId);
    taskRepository.deleteByTaskListId(taskListId);
  }

  private void checkIfTaskListExists(UUID taskListId) {
    if (!taskListGateway.taskListExists(taskListId)) {
      throw new TaskListNotFoundException(taskListId);
    }
  }
}
