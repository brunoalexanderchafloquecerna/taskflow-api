package com.bruno.taskflow_api.user.application.port.in;

import com.bruno.taskflow_api.user.domain.model.User;
import java.util.List;
import java.util.UUID;

public interface UserUseCase {

  User createUser(String email, String name, String password);

  User getUserById(UUID id);

  List<User> getAllUsers();

  User updateUser(UUID id, String email, String name, String oldPassword, String newPassword);

  void deleteUserById(UUID id);

  boolean existsUserById(UUID id);
}
