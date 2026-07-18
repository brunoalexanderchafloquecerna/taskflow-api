package com.bruno.taskflow_api.user.application.exception;

import com.bruno.taskflow_api.shared.domain.exception.NotFoundException;
import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(String message) {
    super(message);
  }
}
