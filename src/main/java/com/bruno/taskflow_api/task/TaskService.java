package com.bruno.taskflow_api.task;

import com.bruno.taskflow_api.task.dto.request.CreateTaskRequest;
import com.bruno.taskflow_api.task.dto.request.UpdateTaskRequest;
import com.bruno.taskflow_api.task.dto.response.TaskResponse;
import com.bruno.taskflow_api.tasklist.TaskList;
import com.bruno.taskflow_api.tasklist.TaskListNotFoundException;
import com.bruno.taskflow_api.tasklist.TaskListRepository;
import com.bruno.taskflow_api.tasklist.dto.response.TaskListSummaryResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

  private final TaskRepository taskRepository;
  private final TaskListRepository taskListRepository;

  public TaskResponse createTask(CreateTaskRequest createTaskRequest) {
    TaskList taskListFound = taskListRepository.findById(createTaskRequest.taskListId())
        .orElseThrow(() -> new TaskListNotFoundException(createTaskRequest.taskListId()));

    LocalDateTime createdAt = LocalDateTime.now();
    Task task = new Task(null, createTaskRequest.title(), createTaskRequest.description(),
        TaskStatus.TODO, taskListFound, createdAt, createdAt);
    Task savedTask = taskRepository.save(task);
    return new TaskResponse(savedTask.getId(), savedTask.getTitle(), savedTask.getDescription(),
        savedTask.getStatus(),
        new TaskListSummaryResponse(taskListFound.getId(), taskListFound.getName()),
        savedTask.getCreatedAt());
  }

  public TaskResponse getTaskById(UUID id) {
    Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),
        new TaskListSummaryResponse(task.getTaskList().getId(), task.getTaskList().getName()),
        task.getCreatedAt());
  }

  public List<TaskResponse> getTasksByStatusAndTaskListId(TaskStatus status, UUID taskListId) {
    return taskRepository.findByStatusAndTaskListId(status, taskListId).stream().map(
        task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(),
            task.getStatus(),
            new TaskListSummaryResponse(task.getTaskList().getId(), task.getTaskList().getName()),
            task.getCreatedAt())).toList();
  }

  public List<TaskResponse> getTasks(UUID taskListId, TaskStatus status) {
     List<Task> tasks;
    if (taskListId != null && status != null) {
      tasks = taskRepository.findByStatusAndTaskListId(status, taskListId);
    } else if (taskListId != null) {
      tasks = taskRepository.findByTaskListId(taskListId);
    } else if (status != null) {
      tasks = taskRepository.findByStatus(status);
    } else {
      tasks = taskRepository.findAll();
    }
    return tasks.stream().map(
        task -> new TaskResponse(task.getId(), task.getTitle(), task.getDescription(),
            task.getStatus(),
            new TaskListSummaryResponse(task.getTaskList().getId(), task.getTaskList().getName()),
            task.getCreatedAt())).collect(Collectors.toList());
  }

  public TaskResponse updateTask(UUID id, UpdateTaskRequest updateTaskRequest) {
    Task taskFound = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    if (updateTaskRequest.title() != null) {
      taskFound.setTitle(updateTaskRequest.title());
    }
    if (updateTaskRequest.description() != null) {
      taskFound.setDescription(updateTaskRequest.description());
    }
    if (updateTaskRequest.status() != null) {
      taskFound.setStatus(updateTaskRequest.status());
    }
    Task updatedTask = taskRepository.save(taskFound);
    return new TaskResponse(updatedTask.getId(), updatedTask.getTitle(),
        updatedTask.getDescription(), updatedTask.getStatus(),
        new TaskListSummaryResponse(updatedTask.getTaskList().getId(),
            updatedTask.getTaskList().getName()), updatedTask.getCreatedAt());
  }

  public void delete(UUID id) {
    taskRepository.deleteById(id);
  }
}
