package com.bruno.taskflow_api.workspace.infrastructure.adapter.out.gateway;

import com.bruno.taskflow_api.user.application.port.in.UserUseCase;
import com.bruno.taskflow_api.workspace.application.port.out.UserGateway;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class UserGatewayAdapter implements UserGateway {

  private final UserUseCase userUseCase;

  public UserGatewayAdapter(UserUseCase userUseCase) {
    this.userUseCase = userUseCase;
  }

  @Override
  public boolean userExistsById(UUID userId) {
    return userUseCase.existsUserById(userId);
  }

  @Override
  public UUID getUserIdByEmail(String currentUsername) {
    return userUseCase.getUserByEmail(currentUsername).getId();
  }
}
