package com.bruno.taskflow_api.tasklist.domain.exception;

public class InvalidTaskListException extends IllegalArgumentException {

  public InvalidTaskListException(String message) {
    super(message);
  }
}
