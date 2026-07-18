package com.bruno.taskflow_api.user.application.service;

import com.bruno.taskflow_api.user.application.exception.UserNotFoundException;
import com.bruno.taskflow_api.user.application.port.in.UserUseCase;
import com.bruno.taskflow_api.user.application.port.out.UserRepository;
import com.bruno.taskflow_api.user.domain.exception.InvalidUserException;
import com.bruno.taskflow_api.user.domain.model.Role;
import com.bruno.taskflow_api.user.domain.model.User;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserUseCase {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
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
  public User updateUser(UUID id, String name, Role role, String email, String oldPassword,
      String newPassword) {
    User userFound = userRepository.findById(id).orElseThrow(
        () -> new UserNotFoundException("User with id %s was not found.".formatted(id)));
    if (!userFound.getPassword().equals(oldPassword)) {
      throw new InvalidUserException("Invalid old password");
    }
    userFound.updateInformation(name, role, email, newPassword);
    return userRepository.save(userFound);
  }

  @Override
  @Transactional
  public void deleteUserById(UUID id) {
    userRepository.deleteById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public boolean existsUserById(UUID id) {
    return userRepository.existsById(id);
  }
}
