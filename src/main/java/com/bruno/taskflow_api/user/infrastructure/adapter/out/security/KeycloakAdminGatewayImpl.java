package com.bruno.taskflow_api.user.infrastructure.adapter.out.security;

import com.bruno.taskflow_api.shared.infrastructure.security.KeycloakProperties;
import com.bruno.taskflow_api.user.application.port.out.KeycloakAdminGateway;
import java.util.List;
import java.util.Map;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KeycloakAdminGatewayImpl implements KeycloakAdminGateway {

  private final RestClient restClient;

  private final KeycloakProperties keycloakProperties;

  public KeycloakAdminGatewayImpl(RestClient restClient, KeycloakProperties keycloakProperties) {
    this.restClient = restClient;
    this.keycloakProperties = keycloakProperties;
  }

  @Override
  public String createUser(String email, String name, String password) {
    String adminToken = obtainServiceAccountToken();

    Map<String, Object> body = Map.of("username", email, "email", email, "firstName", name,
        "enabled", true, "credentials",
        List.of(Map.of("type", "password", "value", password, "temporary", false)));

    ResponseEntity<Void> response = restClient.post()
        .uri("/admin/realms/{realm}/users", keycloakProperties.realm())
        .header("Authorization", "Bearer " + adminToken).body(body).retrieve().toBodilessEntity();

    String location = response.getHeaders().getFirst("Location");
    if (location == null) {
      //throw new KeycloakIntegrationException("Keycloak no devolvió la ubicación del usuario creado");
      throw new RuntimeException("Keycloak no devolvió la ubicación del usuario creado");
    }
    return location.substring(location.lastIndexOf('/') + 1);
  }

  @Override
  public void assignRealmRole(String keycloakId, String roleName) {
    String adminToken = obtainServiceAccountToken();

    RoleRepresentation role = restClient.get()
        .uri("/admin/realms/{realm}/roles/{roleName}", keycloakProperties.realm(), roleName)
        .header("Authorization", "Bearer " + adminToken).retrieve().body(RoleRepresentation.class);

    restClient.post()
        .uri("/admin/realms/{realm}/users/{id}/role-mappings/realm", keycloakProperties.realm(),
            keycloakId).header("Authorization", "Bearer " + adminToken).body(List.of(role))
        .retrieve().toBodilessEntity();
  }

  @Override
  public void deleteUser(String keycloakId) {
    String adminToken = obtainServiceAccountToken();
    restClient.delete()
        .uri("/admin/realms/{realm}/users/{id}", keycloakProperties.realm(), keycloakId)
        .header("Authorization", "Bearer " + adminToken).retrieve().toBodilessEntity();
  }

  private String obtainServiceAccountToken() {
    MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
    form.add("grant_type", "client_credentials");
    form.add("client_id", keycloakProperties.admin().clientId());
    form.add("client_secret", keycloakProperties.admin().clientSecret());

    Map<String, Object> response = restClient.post()
        .uri("/realms/{realm}/protocol/openid-connect/token", keycloakProperties.realm())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(form).retrieve().body(Map.class);

    return (String) response.get("access_token");
  }
}
