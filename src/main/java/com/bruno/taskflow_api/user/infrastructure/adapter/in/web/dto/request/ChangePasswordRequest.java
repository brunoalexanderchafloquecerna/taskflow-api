package com.bruno.taskflow_api.user.infrastructure.adapter.in.web.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(@NotBlank String currentPassword,
                                    @NotBlank @Size(min = 8) String newPassword) {

}
