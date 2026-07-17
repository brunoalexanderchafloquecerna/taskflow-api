package com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.request;

import com.bruno.taskflow_api.user.domain.model.Role;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(@Size(max = 100) String name, @NotNull Role role,
                                @Size(max = 100) String email,
                                @Size(max = 50, min = 8) String oldPassword,
                                @Size(max = 50, min = 8) String newPassword) {

}
