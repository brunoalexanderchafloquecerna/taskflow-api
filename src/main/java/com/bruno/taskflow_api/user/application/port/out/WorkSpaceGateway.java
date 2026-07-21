package com.bruno.taskflow_api.user.application.port.out;

import java.util.UUID;

public interface WorkSpaceGateway {

  boolean userHasWorkspace(UUID userId);
}
