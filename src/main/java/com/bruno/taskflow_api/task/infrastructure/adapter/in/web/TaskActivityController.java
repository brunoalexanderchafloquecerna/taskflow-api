package com.bruno.taskflow_api.task.infrastructure.adapter.in.web;

import com.bruno.taskflow_api.task.application.port.in.GetTaskActivityUseCase;
import com.bruno.taskflow_api.task.domain.model.ActivityEvent;
import com.bruno.taskflow_api.task.infrastructure.adapter.in.web.dto.response.ActivityEventResponse;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskActivityController {

  private final GetTaskActivityUseCase getTaskActivityUseCase;

  public TaskActivityController(GetTaskActivityUseCase getTaskActivityUseCase) {
    this.getTaskActivityUseCase = getTaskActivityUseCase;
  }

  @GetMapping("/{id}/activity")
  public ResponseEntity<List<ActivityEventResponse>> findActivityEventById(@PathVariable UUID id) {
    return ResponseEntity.ok(
        getTaskActivityUseCase.findAllEventsByTaskId(id).stream().map(this::toResponse).toList());
  }

  private ActivityEventResponse toResponse(ActivityEvent activityEvent) {
    return new ActivityEventResponse(activityEvent.taskId(), activityEvent.eventType(),
        activityEvent.timestamp(), activityEvent.payload());
  }
}
