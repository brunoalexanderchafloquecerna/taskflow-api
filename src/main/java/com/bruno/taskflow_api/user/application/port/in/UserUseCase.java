package com.bruno.taskflow_api.user.application.port.in;

import com.bruno.taskflow_api.user.domain.model.Role;
import com.bruno.taskflow_api.user.domain.model.User;
import java.util.List;
import java.util.UUID;

public interface UserUseCase {

  User getUserById(UUID id);

  User getUserByEmail(String email);

  List<User> getAllUsers();

  User updateUser(UUID id, String name, Role role, String email, String oldPassword,
      String newPassword);

  void deleteUserById(UUID id);

  boolean existsUserById(UUID id);
}
