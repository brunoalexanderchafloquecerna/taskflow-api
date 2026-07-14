package com.bruno.taskflow_api.task.dto.response;

import com.bruno.taskflow_api.task.TaskStatus;
import com.bruno.taskflow_api.tasklist.dto.response.TaskListSummaryResponse;
import java.time.LocalDateTime;
import java.util.UUID;

public record TaskResponse(UUID id, String title, String description, TaskStatus status,
                           TaskListSummaryResponse taskList, LocalDateTime createdAt) {

}

