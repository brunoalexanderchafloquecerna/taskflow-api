package com.bruno.taskflow_api.workspace.application.exception;

public class WorkspaceNotFoundException extends RuntimeException {

  public WorkspaceNotFoundException(String message) {
    super(message);
  }
}
