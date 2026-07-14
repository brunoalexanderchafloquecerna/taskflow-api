package com.bruno.taskflow_api.task.dto.request;

import com.bruno.taskflow_api.task.TaskStatus;
import jakarta.validation.constraints.Size;

public record UpdateTaskRequest(@Size(max = 200) String title, @Size(max = 1000) String description,
                                TaskStatus status) {

}
