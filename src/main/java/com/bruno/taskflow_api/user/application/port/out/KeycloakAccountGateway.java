package com.bruno.taskflow_api.user.application.port.out;

public interface KeycloakAccountGateway {

  void changePassword(String userToken, String currentPassword, String newPassword);

  void updateProfile(String userToken, String email, String name);
}
