package com.bruno.taskflow_api.shared.infrastructure.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

  @Override
  public Collection<GrantedAuthority> convert(Jwt source) {
    Map<String, Object> claims = source.getClaims();

    Object realmAccessClaim = claims.get("realm_access");

    if (!(realmAccessClaim instanceof Map<?, ?> realmAccess)) {
      return List.of();
    }

    Object rolesClaim = realmAccess.get("roles");
    if (!(rolesClaim instanceof List<?> roles)) {
      return List.of();
    }

    return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
        .collect(Collectors.toList());
  }
}
