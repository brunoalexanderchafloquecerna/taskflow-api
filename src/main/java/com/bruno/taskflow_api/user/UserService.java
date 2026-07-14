package com.bruno.taskflow_api.user;

import com.bruno.taskflow_api.user.dto.request.CreateUserRequest;
import com.bruno.taskflow_api.user.dto.response.UserResponse;
import java.util.UUID;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserResponse create(CreateUserRequest createUserRequest) {
    userRepository.findByEmail(createUserRequest.email()).ifPresent(userFound -> {
      throw new DataIntegrityViolationException(
          "User with email " + userFound.getEmail() + " already exists");
    });
    User userSaved = userRepository.save(
        new User(null, createUserRequest.email(), createUserRequest.name(),
            createUserRequest.password()));
    return new UserResponse(userSaved.getId(), userSaved.getEmail(), userSaved.getName());
  }

  public UserResponse findById(UUID id) {
    User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    return new UserResponse(user.getId(), user.getEmail(), user.getName());
  }
}
