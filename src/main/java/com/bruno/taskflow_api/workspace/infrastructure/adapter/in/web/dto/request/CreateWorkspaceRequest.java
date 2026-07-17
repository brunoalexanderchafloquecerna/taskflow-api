package com.bruno.taskflow_api.workspace.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreateWorkspaceRequest(@NotBlank String name, @NotNull UUID userId) {

}
