package com.bruno.taskflow_api.user.application.exception;

public class UserHasActiveResourcesException extends RuntimeException {

  public UserHasActiveResourcesException(String message) {
    super(message);
  }
}
