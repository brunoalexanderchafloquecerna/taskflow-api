package com.bruno.taskflow_api.user.application.port.out;

public interface KeycloakAdminGateway {

  String createUser(String email, String name, String password);

  void assignRealmRole(String keycloakId, String roleName);

  void deleteUser(String keycloakId);
}
