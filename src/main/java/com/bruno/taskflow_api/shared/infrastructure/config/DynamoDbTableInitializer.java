package com.bruno.taskflow_api.shared.infrastructure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ResourceNotFoundException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

// TODO Día 11/13: restringir con @Profile("local") cuando se formalicen
// los profiles de Spring para distinguir despliegue local vs real
@Configuration
public class DynamoDbTableInitializer {

  private static final Logger log = LoggerFactory.getLogger(DynamoDbTableInitializer.class);

  @Bean
  ApplicationRunner createTaskflowEventsTableIfNotExists(
      DynamoDbClient dynamoDbClient,
      @Value("${aws.dynamodb.table-name}") String tableName) {
    System.out.println("BEAN METODO EJECUTADO");
    return args -> {
      if (tableExists(dynamoDbClient, tableName)) {
        log.info("Tabla DynamoDB '{}' ya existe, omitiendo creación", tableName);
        return;
      }

      log.info("Tabla DynamoDB '{}' no existe, creando...", tableName);

      dynamoDbClient.createTable(CreateTableRequest.builder()
          .tableName(tableName)
          .billingMode(BillingMode.PAY_PER_REQUEST)
          .attributeDefinitions(
              AttributeDefinition.builder()
                  .attributeName("pk")
                  .attributeType(ScalarAttributeType.S)
                  .build(),
              AttributeDefinition.builder()
                  .attributeName("sk")
                  .attributeType(ScalarAttributeType.S)
                  .build())
          .keySchema(
              KeySchemaElement.builder()
                  .attributeName("pk")
                  .keyType(KeyType.HASH)
                  .build(),
              KeySchemaElement.builder()
                  .attributeName("sk")
                  .keyType(KeyType.RANGE)
                  .build())
          .build());

      log.info("Tabla DynamoDB '{}' creada", tableName);

      dynamoDbClient.waiter().waitUntilTableExists(
          builder -> builder.tableName(tableName).build());

      log.info("Tabla DynamoDB '{}' creada y activa", tableName);
    };
  }

  private boolean tableExists(DynamoDbClient client, String tableName) {
    try {
      client.describeTable(builder -> builder.tableName(tableName).build());
      return true;
    } catch (ResourceNotFoundException e) {
      return false;
    }
  }
}