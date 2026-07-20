package com.bruno.taskflow_api.user.infrastructure.adapter.out.security;

import com.bruno.taskflow_api.shared.infrastructure.security.KeycloakProperties;
import com.bruno.taskflow_api.user.application.port.out.KeycloakAccountGateway;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

@Component
public class KeycloakAccountGatewayImpl implements KeycloakAccountGateway {

  private final RestClient restClient;

  private final KeycloakProperties props;

  public KeycloakAccountGatewayImpl(RestClient restClient, KeycloakProperties props) {
    this.restClient = restClient;
    this.props = props;
  }

  @Override
  public void changePassword(String userToken, String currentPassword, String newPassword) {
    Map<String, Object> body = Map.of("currentPassword", currentPassword, "newPassword",
        newPassword);

    try {
      restClient.post().uri("/realms/{realm}/account/credentials/password", props.realm())
          .header("Authorization", userToken).body(body).retrieve().toBodilessEntity();
    } catch (HttpClientErrorException.BadRequest e) {
      //throw new InvalidCurrentPasswordException("La contraseña actual no es correcta");
      throw new RuntimeException("La contraseña actual no es correcta");
    }
  }

  @Override
  public void updateProfile(String userToken, String email, String name) {
    Map<String, Object> body = Map.of("email", email, "firstName", name, "username", email);

    restClient.post().uri("/realms/{realm}/account", props.realm())
        .header("Authorization", userToken).body(body).retrieve().toBodilessEntity();
  }
}
