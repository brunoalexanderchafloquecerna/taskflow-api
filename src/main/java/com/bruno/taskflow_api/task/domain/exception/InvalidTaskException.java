package com.bruno.taskflow_api.task.domain.exception;

import com.bruno.taskflow_api.shared.domain.exception.DomainException;

public class InvalidTaskException extends DomainException {

  public InvalidTaskException(String message) {
    super(message);
  }
}
