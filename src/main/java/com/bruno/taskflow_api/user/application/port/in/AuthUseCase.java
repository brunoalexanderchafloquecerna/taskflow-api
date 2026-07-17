package com.bruno.taskflow_api.user.application.port.in;

import com.bruno.taskflow_api.user.application.dto.response.AuthResponse;
import com.bruno.taskflow_api.user.domain.model.Role;

public interface AuthUseCase {

  AuthResponse register(String name, Role role, String email, String password);

  AuthResponse login(String email, String password);
}
