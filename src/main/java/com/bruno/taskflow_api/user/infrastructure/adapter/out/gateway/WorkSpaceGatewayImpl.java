package com.bruno.taskflow_api.user.infrastructure.adapter.out.gateway;

import com.bruno.taskflow_api.user.application.port.out.WorkSpaceGateway;
import com.bruno.taskflow_api.workspace.application.port.in.WorkspaceUseCase;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
public class WorkSpaceGatewayImpl implements WorkSpaceGateway {

  private final WorkspaceUseCase workspaceUseCase;

  public WorkSpaceGatewayImpl(WorkspaceUseCase workspaceUseCase) {
    this.workspaceUseCase = workspaceUseCase;
  }

  @Override
  public boolean userHasWorkspace(UUID userId) {
    return workspaceUseCase.existsByUserId(userId);
  }
}
