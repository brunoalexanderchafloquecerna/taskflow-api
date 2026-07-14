package com.bruno.taskflow_api.tasklist.dto.response;

import com.bruno.taskflow_api.task.TaskStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskListResponse(UUID id, String title, String description, TaskStatus status,
                               TaskListSummaryResponse taskList, LocalDateTime createdAt) {

}

