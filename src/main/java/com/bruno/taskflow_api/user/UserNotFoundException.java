package com.bruno.taskflow_api.user;

import com.bruno.taskflow_api.NotFoundException;
import java.util.UUID;

public class UserNotFoundException extends NotFoundException {

  public UserNotFoundException(UUID id) {
    super("User not found with ID: " + id);
  }
}
