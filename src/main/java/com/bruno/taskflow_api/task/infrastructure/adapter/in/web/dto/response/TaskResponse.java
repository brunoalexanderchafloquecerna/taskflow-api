package com.bruno.taskflow_api.task.infrastructure.adapter.in.web.dto.response;

import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(UUID id, String title, String description, TaskStatus status,
                           UUID taskList, LocalDateTime createdAt, LocalDateTime updatedAt) {

}

