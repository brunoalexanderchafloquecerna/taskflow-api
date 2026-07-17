package com.bruno.taskflow_api.user.infrastructure.adapter.out.security;

import com.bruno.taskflow_api.user.application.port.out.UserRepository;
import com.bruno.taskflow_api.user.domain.model.User;
import java.util.List;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new UsernameNotFoundException("User with email " + email + " was not found"));
    return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(),
        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
  }
}
