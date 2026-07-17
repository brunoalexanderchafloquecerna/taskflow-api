package com.bruno.taskflow_api.workspace.application.exception;

import com.bruno.taskflow_api.shared.domain.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(String message) {
    super(message);
  }
}
