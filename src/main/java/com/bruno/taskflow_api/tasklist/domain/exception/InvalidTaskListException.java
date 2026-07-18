package com.bruno.taskflow_api.tasklist.domain.exception;

import com.bruno.taskflow_api.shared.domain.exception.DomainException;

public class InvalidTaskListException extends DomainException {

  public InvalidTaskListException(String message) {
    super(message);
  }
}
