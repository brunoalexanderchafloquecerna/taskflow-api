package com.bruno.taskflow_api.user.application.service;

import com.bruno.taskflow_api.user.application.exception.UserHasActiveResourcesException;
import com.bruno.taskflow_api.user.application.exception.UserNotFoundException;
import com.bruno.taskflow_api.user.application.port.in.UserUseCase;
import com.bruno.taskflow_api.user.application.port.out.KeycloakAccountGateway;
import com.bruno.taskflow_api.user.application.port.out.KeycloakAdminGateway;
import com.bruno.taskflow_api.user.application.port.out.UserRepository;
import com.bruno.taskflow_api.user.application.port.out.WorkSpaceGateway;
import com.bruno.taskflow_api.user.domain.model.User;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserUseCase {

  private final UserRepository userRepository;

  private final KeycloakAdminGateway keycloakAdminGateway;

  private final KeycloakAccountGateway keycloakAccountGateway;

  private final WorkSpaceGateway workSpaceGateway;

  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public UserService(UserRepository userRepository, KeycloakAdminGateway keycloakAdminGateway,
      KeycloakAccountGateway keycloakAccountGateway, WorkSpaceGateway workSpaceGateway) {
    this.userRepository = userRepository;
    this.keycloakAdminGateway = keycloakAdminGateway;
    this.keycloakAccountGateway = keycloakAccountGateway;
    this.workSpaceGateway = workSpaceGateway;
  }

  @Override
  @Transactional(readOnly = true)
  public User getUserById(UUID id) {
    return userRepository.findById(id).orElseThrow(
        () -> new UserNotFoundException("User with id %s was not found.".formatted(id)));
  }

  @Override
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email).orElseThrow(
        () -> new UserNotFoundException("User with email %s was not found.".formatted(email)));
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  @Transactional
  public void deleteUserById(UUID id) {
    User user = getUserById(id);
    if (workSpaceGateway.userHasWorkspace(user.getId())) {
      throw new UserHasActiveResourcesException("You cannot delete a user with active resources");
    }
    keycloakAdminGateway.deleteUser(user.getKeycloakId());
    userRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsUserById(UUID id) {
    return userRepository.existsById(id);
  }

  @Override
  @Transactional
  public void register(String email, String name, String password) {
    String keycloakId = keycloakAdminGateway.createUser(email, name, password);
    keycloakAdminGateway.assignRealmRole(keycloakId, "USER");
    logger.info("New User with email {} registered successfully with keycloakId {}", email,
        keycloakId);
  }

  @Override
  @Transactional
  public void changePassword(String userToken, String currentPassword, String newPassword) {
    keycloakAccountGateway.changePassword(userToken, currentPassword, newPassword);
  }

  @Override
  @Transactional
  public void updateOwnProfile(UUID userId, String userToken, String newEmail, String newName) {
    keycloakAccountGateway.updateProfile(userToken, newEmail, newName);
    User user = getUserById(userId);
    user.updateName(newName);
    user.updateEmail(newEmail);
    userRepository.save(user);
  }

  @Override
  @Transactional
  public void promoteToAdmin(UUID userId) {
    User user = getUserById(userId);
    keycloakAdminGateway.assignRealmRole(user.getKeycloakId(), "ADMIN");
  }
}
