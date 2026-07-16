package com.bruno.taskflow_api.tasklist.infrastructure.adapter.in.web;

import com.bruno.taskflow_api.tasklist.application.port.in.TaskListUseCase;
import com.bruno.taskflow_api.tasklist.domain.model.TaskList;
import com.bruno.taskflow_api.tasklist.infrastructure.adapter.in.web.dto.request.CreateTaskListRequest;
import com.bruno.taskflow_api.tasklist.infrastructure.adapter.in.web.dto.response.TaskListResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasklists")
public class TaskListController {

  private final TaskListUseCase taskListUseCase;

  public TaskListController(TaskListUseCase taskListUseCase) {
    this.taskListUseCase = taskListUseCase;
  }

  @PostMapping
  public ResponseEntity<TaskListResponse> create(
      @RequestBody @Valid CreateTaskListRequest createTaskListRequest) {
    TaskList taskList = taskListUseCase.createTaskList(createTaskListRequest.name(),
        createTaskListRequest.workspaceId(), createTaskListRequest.position());
    return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(taskList));
  }

  @GetMapping("/{id}")
  public ResponseEntity<TaskListResponse> getTaskListById(@PathVariable UUID id) {
    return ResponseEntity.ok(toResponse(taskListUseCase.getTaskListById(id)));
  }

  @GetMapping
  public ResponseEntity<List<TaskListResponse>> getTaskLists(
      @RequestParam(required = false) UUID workspaceId) {
    return ResponseEntity.ok(
        taskListUseCase.getTaskListsByFilters(workspaceId).stream().map(this::toResponse).toList());
  }

  @PutMapping("/{id}")
  public ResponseEntity<TaskListResponse> update(@PathVariable UUID id,
      @RequestBody @Valid CreateTaskListRequest createTaskListRequest) {
    TaskList taskList = taskListUseCase.updateTaskListById(id, createTaskListRequest.name(),
        createTaskListRequest.workspaceId(), createTaskListRequest.position());
    return ResponseEntity.ok(toResponse(taskList));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    taskListUseCase.deleteTaskListById(id);
    return ResponseEntity.ok().build();
  }

  private TaskListResponse toResponse(TaskList taskList) {
    return new TaskListResponse(taskList.getId(), taskList.getName(), taskList.getWorkspaceId(),
        taskList.getPosition());
  }
}
