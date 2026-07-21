package com.bruno.taskflow_api.shared.infrastructure.security;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(KeycloakProperties.class)
public class KeycloakClientConfig {

  @Bean
  public RestClient keycloakRestClient(KeycloakProperties properties) {
    return RestClient.builder().baseUrl(properties.baseUrl()).build();
  }
}
