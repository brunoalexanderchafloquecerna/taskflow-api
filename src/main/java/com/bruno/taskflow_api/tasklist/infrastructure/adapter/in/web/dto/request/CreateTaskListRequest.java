package com.bruno.taskflow_api.tasklist.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;

public record CreateTaskListRequest(@NotBlank String name, @NotNull UUID workspaceId,
                                    @PositiveOrZero int position) {

}
