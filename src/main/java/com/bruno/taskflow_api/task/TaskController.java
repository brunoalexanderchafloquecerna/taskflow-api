package com.bruno.taskflow_api.task;

import com.bruno.taskflow_api.task.dto.request.CreateTaskRequest;
import com.bruno.taskflow_api.task.dto.request.UpdateTaskRequest;
import com.bruno.taskflow_api.task.dto.response.TaskResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @PostMapping
  public ResponseEntity<TaskResponse> create(
      @Valid @RequestBody CreateTaskRequest createTaskRequest) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(taskService.createTask(createTaskRequest));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskResponse> getTaskById(@PathVariable UUID id) {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.getTaskById(id));
  }

  @GetMapping
  public ResponseEntity<List<TaskResponse>> getTasks(
      @RequestParam(required = false) UUID taskListId,
      @RequestParam(required = false) TaskStatus status) {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.getTasks(taskListId, status));
  }

  @PatchMapping("/{id}")
  public ResponseEntity<TaskResponse> updateTask(@PathVariable UUID id,
      @Valid @RequestBody UpdateTaskRequest updateTaskRequest) {
    return ResponseEntity.status(HttpStatus.OK).body(taskService.updateTask(id, updateTaskRequest));
  }

  @DeleteMapping("/{id}")
  public void deleteTask(@PathVariable UUID id) {
    taskService.delete(id);
  }
}
