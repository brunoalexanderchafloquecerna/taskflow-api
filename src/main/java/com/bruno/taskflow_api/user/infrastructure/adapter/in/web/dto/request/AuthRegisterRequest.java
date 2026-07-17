package com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.request;

import com.bruno.taskflow_api.user.domain.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthRegisterRequest(@NotBlank String name, @NotNull Role role,
                                  @NotBlank @Email String email,
                                  @NotBlank @Size(min = 8, message = "The password must be at least 8 characters long") String password) {

}
