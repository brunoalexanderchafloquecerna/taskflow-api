package com.bruno.taskflow_api.user.application.exception;

import com.bruno.taskflow_api.shared.domain.exception.DomainException;

public class UserAlreadyInUseException extends DomainException {

  public UserAlreadyInUseException(String message) {
    super(message);
  }
}
