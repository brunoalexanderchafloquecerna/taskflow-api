package com.bruno.taskflow_api.workspace.infrastructure.adapter.in.web.dto.response;

import java.util.UUID;

public record WorkspaceResponse(UUID id, String name, UUID userId) {

}
