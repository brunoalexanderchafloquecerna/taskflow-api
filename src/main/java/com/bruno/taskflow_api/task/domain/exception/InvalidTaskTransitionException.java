package com.bruno.taskflow_api.task.domain.exception;

import com.bruno.taskflow_api.shared.domain.exception.DomainException;

public class InvalidTaskTransitionException extends DomainException {

  public InvalidTaskTransitionException(String message) {
    super(message);
  }
}
