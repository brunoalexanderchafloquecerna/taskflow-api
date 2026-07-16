package com.bruno.taskflow_api.user.application.exception;

import com.bruno.taskflow_api.shared.domain.exception.NotFoundException;
import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(UUID id) {
    super("User not found with ID: " + id);
  }
}
