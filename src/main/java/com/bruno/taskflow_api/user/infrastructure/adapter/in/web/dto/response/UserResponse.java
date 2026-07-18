package com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.response;

import java.util.UUID;

public record UserResponse(UUID id, String email, String name) {

}
