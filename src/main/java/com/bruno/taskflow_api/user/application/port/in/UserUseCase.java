package com.bruno.taskflow_api.user.application.port.in;

import com.bruno.taskflow_api.user.domain.model.User;
import java.util.List;
import java.util.UUID;

public interface UserUseCase {

  User getUserById(UUID id);

  User getUserByEmail(String email);

  List<User> getAllUsers();

  void deleteUserById(UUID id);

  boolean existsUserById(UUID id);

  void register(String email, String name, String password);

  void changePassword(String userToken, String currentPassword, String newPassword);

  void updateOwnProfile(UUID userId, String userToken, String newEmail, String newName);

  void promoteToAdmin(UUID userId);
}
