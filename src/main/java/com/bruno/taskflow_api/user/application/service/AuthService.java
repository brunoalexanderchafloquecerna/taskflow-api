package com.bruno.taskflow_api.user.application.service;

import com.bruno.taskflow_api.shared.infrastructure.security.JwtService;
import com.bruno.taskflow_api.user.application.dto.response.AuthResponse;
import com.bruno.taskflow_api.user.application.exception.UserAlreadyInUseException;
import com.bruno.taskflow_api.user.application.port.in.AuthUseCase;
import com.bruno.taskflow_api.user.application.port.out.UserRepository;
import com.bruno.taskflow_api.user.domain.model.Role;
import com.bruno.taskflow_api.user.domain.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService implements AuthUseCase {

  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private final AuthenticationManager authenticationManager;

  private final JwtService jwtService;

  private final UserDetailsService userDetailsService;

  public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      AuthenticationManager authenticationManager, JwtService jwtService,
      UserDetailsService userDetailsService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
    this.userDetailsService = userDetailsService;
  }

  @Override
  @Transactional
  public AuthResponse register(String name, Role role, String email, String password) {
    if (userRepository.existsByEmail(email)) {
      throw new UserAlreadyInUseException("Email %s already in use.".formatted(email));
    }
    String hashedPassword = passwordEncoder.encode(password);
    User user = User.create(name, role, email, hashedPassword);
    userRepository.save(user);
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    String token = jwtService.generateToken(userDetails);
    return new AuthResponse(token);
  }

  @Override
  @Transactional
  public AuthResponse login(String email, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    String token = jwtService.generateToken(userDetails);
    return new AuthResponse(token);
  }
}
