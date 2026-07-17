package com.bruno.taskflow_api.task.infrastructure.adapter.in.web;

import com.bruno.taskflow_api.task.application.port.in.TaskUseCase;
import com.bruno.taskflow_api.task.application.service.TaskService;
import com.bruno.taskflow_api.task.domain.model.Task;
import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import com.bruno.taskflow_api.task.infrastructure.adapter.in.web.dto.request.CreateTaskRequest;
import com.bruno.taskflow_api.task.infrastructure.adapter.in.web.dto.request.UpdateTaskRequest;
import com.bruno.taskflow_api.task.infrastructure.adapter.in.web.dto.response.TaskResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskUseCase taskUseCase;

  public TaskController(TaskService taskUseCase) {
    this.taskUseCase = taskUseCase;
  }

  @PostMapping
  public ResponseEntity<TaskResponse> create(
      @Valid @RequestBody CreateTaskRequest createTaskRequest) {
    Task task = taskUseCase.createTask(createTaskRequest.title(), createTaskRequest.description(),
        createTaskRequest.taskListId());
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(task));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(toResponse(taskUseCase.getTaskById(id)));
  }

  @GetMapping
  public ResponseEntity<List<TaskResponse>> getTasks(
      @RequestParam(required = false) UUID taskListId,
      @RequestParam(required = false) TaskStatus status) {
    return ResponseEntity.status(HttpStatus.OK).body(
        taskUseCase.getTasksByFilters(taskListId, status).stream().map(this::toResponse).toList());
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskResponse> updateTask(@PathVariable UUID id,
      @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
    return ResponseEntity.status(HttpStatus.OK).body(toResponse(
        taskUseCase.updateTaskInformation(id, updateTaskRequest.title(),
            updateTaskRequest.description(), updateTaskRequest.status(),
            updateTaskRequest.taskListId())));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<TaskResponse> updateStatusTask(@PathVariable UUID id,
      @RequestParam TaskStatus status) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(toResponse(taskUseCase.changeTaskStatus(id, status)));
  }

  @DeleteMapping("/{id}")
  public void deleteTask(@PathVariable UUID id) {
    taskUseCase.deleteTask(id);
  }

  @DeleteMapping("/")
  public void deleteTasksByTaskListId(@RequestParam UUID taskListId) {
    taskUseCase.deleteTasksByTaskListId(taskListId);
  }

  private TaskResponse toResponse(Task task) {
    return new TaskResponse(task.getId(), task.getTitle(), task.getDescription(), task.getStatus(),
        task.getTaskListId(), task.getCreatedAt(), task.getUpdatedAt());
  }
}
