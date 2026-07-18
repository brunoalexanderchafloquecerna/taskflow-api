package com.bruno.taskflow_api.workspace.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateWorkspaceRequest(@NotBlank String name) {

}
