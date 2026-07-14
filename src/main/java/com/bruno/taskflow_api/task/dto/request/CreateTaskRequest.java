package com.bruno.taskflow_api.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateTaskRequest(
    @NotBlank(message = "The title cannot be blank") @Size(max = 200) String title,
    @NotBlank(message = "The description cannot be blank") @Size(max = 1000) String description,
    @NotNull(message = "The task list ID cannot be null") UUID taskListId) {

}
