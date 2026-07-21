package com.bruno.taskflow_api.shared.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "keycloak")
public record KeycloakProperties(String baseUrl, String realm, Admin admin) {

  public record Admin(String clientId, String clientSecret) {

  }
}
