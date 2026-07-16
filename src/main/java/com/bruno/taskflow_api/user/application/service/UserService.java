package com.bruno.taskflow_api.user.application.service;

import com.bruno.taskflow_api.user.application.exception.UserNotFoundException;
import com.bruno.taskflow_api.user.application.port.in.UserUseCase;
import com.bruno.taskflow_api.user.application.port.out.UserRepository;
import com.bruno.taskflow_api.user.domain.exception.InvalidUserException;
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
  @Transactional
  public User createUser(String email, String name, String password) {
    if (userRepository.existsByEmail(email)) {
      throw new InvalidUserException("User with email " + email + " already exists");
    }
    return userRepository.save(User.create(email, name, password));
  }

  @Override
  @Transactional(readOnly = true)
  public User getUserById(UUID id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  @Override
  @Transactional
  public User updateUser(UUID id, String email, String name, String oldPassword,
      String newPassword) {
    User userFound = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    if (!userFound.getPassword().equals(oldPassword)) {
      throw new InvalidUserException("Invalid old password");
    }
    userFound.updateInformation(email, name, newPassword);
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
