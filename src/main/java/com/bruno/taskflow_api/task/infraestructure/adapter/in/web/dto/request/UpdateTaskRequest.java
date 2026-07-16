package com.bruno.taskflow_api.task.infraestructure.adapter.in.web.dto.request;

import com.bruno.taskflow_api.task.domain.model.TaskStatus;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UpdateTaskRequest(@Size(max = 200) String title, @Size(max = 1000) String description,
                                TaskStatus status, UUID taskListId) {

}
