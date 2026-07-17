package com.bruno.taskflow_api.workspace.application.port.out;

import java.util.UUID;

public interface UserGateway {

  boolean userExistsById(UUID userId);

  UUID getUserIdByEmail(String currentUsername);
}
