package com.bruno.taskflow_api.task.application.service;

import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import com.bruno.taskflow_api.shared.application.utils.ValidationUtils;
import com.bruno.taskflow_api.task.application.event.TaskCompletedIntegrationEvent;
import com.bruno.taskflow_api.task.application.exception.TaskListNotFoundException;
import com.bruno.taskflow_api.task.application.exception.TaskNotFoundException;
import com.bruno.taskflow_api.task.application.port.in.TaskUseCase;
import com.bruno.taskflow_api.task.application.port.out.ActivityLogPort;
import com.bruno.taskflow_api.task.application.port.out.TaskEventPublisher;
import com.bruno.taskflow_api.task.application.port.out.TaskListGateway;
import com.bruno.taskflow_api.task.application.port.out.TaskRepository;
import com.bruno.taskflow_api.task.domain.model.ActivityEvent;
import com.bruno.taskflow_api.task.domain.model.Task;
import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskService implements TaskUseCase {

  private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
  private final TaskRepository taskRepository;
  private final TaskListGateway taskListGateway;
  private final ActivityLogPort activityLogPort;
  private final AuthenticatedUserProvider authenticatedUserProvider;
  private final TaskEventPublisher taskEventPublisher;

  public TaskService(TaskRepository taskRepository, TaskListGateway taskListGateway,
      ActivityLogPort activityLogPort, AuthenticatedUserProvider authenticatedUserProvider,
      TaskEventPublisher taskEventPublisher) {
    this.taskRepository = taskRepository;
    this.taskListGateway = taskListGateway;
    this.activityLogPort = activityLogPort;
    this.authenticatedUserProvider = authenticatedUserProvider;
    this.taskEventPublisher = taskEventPublisher;
  }

  @Override
  @Transactional
  public Task createTask(String title, String description, UUID taskListId) {
    this.checkIfTaskListExists(taskListId);
    UUID currentUserId = authenticatedUserProvider.getCurrentUserId();
    Task task = taskRepository.save(Task.create(title, description, taskListId, currentUserId));
    try {
      activityLogPort.record(
          new ActivityEvent(task.getId().toString(), "TASK_CREATED", currentUserId.toString(),
              task.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant(), "{}"));
    } catch (Exception e) {
      LOGGER.error("The activity could not be recorded for the task {}: {}", task.getId(),
          e.getMessage());
    }
    return task;
  }

  @Override
  @Transactional
  public Task changeTaskStatus(UUID id, TaskStatus status) {
    Task taskFound = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    ValidationUtils.isOwnerOrAdmin(authenticatedUserProvider, taskFound.getOwnerId());
    taskFound.moveTo(status);
    Task task = taskRepository.save(taskFound);
    try {
      activityLogPort.record(new ActivityEvent(task.getId().toString(), "TASK_CHANGED_STATUS",
          authenticatedUserProvider.getCurrentUserId().toString(),
          task.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant(), "{}"));
    } catch (Exception e) {
      LOGGER.error("The activity could not be recorded for the task {}: {}", task.getId(),
          e.getMessage());
    }
    if (status == TaskStatus.DONE) {
      taskEventPublisher.publishTaskCompleted(
          new TaskCompletedIntegrationEvent(task.getId(), task.getTaskListId(), task.getOwnerId(),
              authenticatedUserProvider.getCurrentUserId(), Instant.now()));
    }
    return task;
  }

  @Override
  @Transactional
  public Task updateTaskInformation(UUID id, String title, String description, TaskStatus status,
      UUID taskListId) {
    Task taskFound = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    ValidationUtils.isOwnerOrAdmin(authenticatedUserProvider, taskFound.getOwnerId());
    checkIfTaskListExists(taskListId);
    taskFound.updateInformation(title, description, status, taskListId);
    Task task = taskRepository.save(taskFound);
    try {
      activityLogPort.record(new ActivityEvent(task.getId().toString(), "TASK_UPDATED",
          authenticatedUserProvider.getCurrentUserId().toString(),
          task.getUpdatedAt().atZone(ZoneId.systemDefault()).toInstant(), "{}"));
    } catch (Exception e) {
      LOGGER.error("The activity could not be recorded for the task {}: {}", task.getId(),
          e.getMessage());
    }
    return task;
  }

  @Override
  @Transactional(readOnly = true)
  public Task getTaskById(UUID id) {
    return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<Task> getTasksByFilters(UUID taskListId, TaskStatus status) {
    UUID currentUserId = authenticatedUserProvider.getCurrentUserId();
    return taskRepository.findTasksByFilters(currentUserId, taskListId, status);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Task> getAllTasks() {
    boolean isAdmin = authenticatedUserProvider.currentUserIsAdmin();
    if (!isAdmin) {
      throw new AccessDeniedException("Only admins can view all tasks");
    }
    return taskRepository.findAllTasks();
  }

  @Override
  @Transactional
  public void deleteTask(UUID id) {
    Task taskFound = getTaskById(id);
    ValidationUtils.isOwnerOrAdmin(authenticatedUserProvider, taskFound.getOwnerId());
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
