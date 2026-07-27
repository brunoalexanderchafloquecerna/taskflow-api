package com.bruno.taskflow_api.shared.infrastructure.config;

import java.net.URI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.http.urlconnection.UrlConnectionHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

@Configuration
public class DynamoDbConfig {

  @Value("${aws.dynamodb.endpoint}")
  private URI dynamodbEndpoint;

  @Value("${aws.dynamodb.region}")
  private String dynamodbRegion;

  @Value("${aws.dynamodb.access-key-id}")
  private String accessKeyId;

  @Value("${aws.dynamodb.secret-access-key}")
  private String secretAccessKey;

  @Bean
  public DynamoDbClient dynamoDbClient() {
    AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(accessKeyId,
        secretAccessKey);
    return DynamoDbClient.builder().endpointOverride(dynamodbEndpoint)
        .region(Region.of(dynamodbRegion))
        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials)).httpClient(
            UrlConnectionHttpClient.create()).build();
  }

  @Bean
  public DynamoDbEnhancedClient dynamoDbEnhancedClient(DynamoDbClient dynamoDbClient) {
    System.out.println("ENDPOINT REAL USADO: " + dynamodbEndpoint);
    return DynamoDbEnhancedClient.builder().dynamoDbClient(dynamoDbClient).build();
  }
}
