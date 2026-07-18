package com.bruno.taskflow_api.workspace.application.exception;

import com.bruno.taskflow_api.shared.domain.exception.NotFoundException;

public class WorkspaceNotFoundException extends NotFoundException {

  public WorkspaceNotFoundException(String message) {
    super(message);
  }
}
