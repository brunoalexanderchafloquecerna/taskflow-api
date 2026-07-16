package com.bruno.taskflow_api.workspace.domain.exception;

import com.bruno.taskflow_api.shared.domain.exception.DomainException;

public class InvalidWorkspaceException extends DomainException {

  public InvalidWorkspaceException(String message) {
    super(message);
  }
}
