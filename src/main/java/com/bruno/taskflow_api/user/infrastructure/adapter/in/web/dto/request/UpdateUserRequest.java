package com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.Size;

public record UpdateUserRequest(@Size(max = 100) String name,
                                @Size(max = 100) String email,
                                @Size(max = 50, min = 8) String oldPassword,
                                @Size(max = 50, min = 8) String newPassword) {

}
