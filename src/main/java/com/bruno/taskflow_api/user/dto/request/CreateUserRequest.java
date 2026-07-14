package com.bruno.taskflow_api.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(@NotBlank @Size(max = 100) String email,
                                @NotBlank @Size(max = 100) String name,
                                @NotBlank @Size(max = 50, min = 8) String password) {

}
