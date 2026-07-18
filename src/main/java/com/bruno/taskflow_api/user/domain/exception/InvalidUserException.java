package com.bruno.taskflow_api.user.domain.exception;

import com.bruno.taskflow_api.shared.domain.exception.DomainException;

public class InvalidUserException extends DomainException {

  public InvalidUserException(String message) {
    super(message);
  }
}
