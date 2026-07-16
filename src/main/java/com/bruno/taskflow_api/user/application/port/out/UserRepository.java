package com.bruno.taskflow_api.user.application.port.out;

import com.bruno.taskflow_api.user.domain.model.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

  User save(User user);

  Optional<User> findById(UUID id);

  List<User> findAll();
  
  boolean existsByEmail(String email);

  void deleteById(UUID id);

  boolean existsById(UUID id);
}
