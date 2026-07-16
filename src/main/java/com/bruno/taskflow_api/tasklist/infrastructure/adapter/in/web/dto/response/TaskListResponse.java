package com.bruno.taskflow_api.tasklist.infrastructure.adapter.in.web.dto.response;

import java.util.UUID;

public record TaskListResponse(UUID id, String name, UUID workspaceId, int position) {

}

