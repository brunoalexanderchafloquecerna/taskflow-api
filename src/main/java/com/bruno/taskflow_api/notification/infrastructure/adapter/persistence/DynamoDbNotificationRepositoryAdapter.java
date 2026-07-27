package com.bruno.taskflow_api.notification.infrastructure.adapter.persistence;

import com.bruno.taskflow_api.notification.domain.model.NotificationEvent;
import com.bruno.taskflow_api.notification.application.port.out.NotificationRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;

@Repository
public class DynamoDbNotificationRepositoryAdapter implements NotificationRepository {

  private final DynamoDbTable<NotificationItem> table;

  public DynamoDbNotificationRepositoryAdapter(DynamoDbEnhancedClient enhancedClient,
      @Value("${aws.dynamodb.table-name}") String tableName) {
    this.table = enhancedClient.table(tableName, TableSchema.fromBean(NotificationItem.class));
  }

  @Override
  public void record(NotificationEvent event) {
    table.putItem(toItem(event));
  }

  @Override
  public List<NotificationEvent> findByUserId(String userId) {
    QueryConditional queryConditional = QueryConditional.keyEqualTo(
        Key.builder().partitionValue(NotificationKeys.buildPk(userId)).build());

    return table.query(queryConditional).items().stream()
        .filter(notificationItem -> Instant.ofEpochSecond(notificationItem.getTtl()).isAfter(Instant.now()))
        .map(this::toDomain).toList();
  }

  private NotificationItem toItem(NotificationEvent event) {
    NotificationItem item = new NotificationItem();
    item.setPk(NotificationKeys.buildPk(event.userId()));
    item.setSk(NotificationKeys.buildSk(event.timestamp()));
    item.setTtl(event.expiresAt().getEpochSecond());
    item.setUserId(event.userId());
    item.setEventType(event.eventType());
    item.setActorId(event.actorId());
    item.setTimestamp(event.timestamp().toString());
    item.setPayload(event.payload());
    return item;
  }

  private NotificationEvent toDomain(NotificationItem notificationItem) {
    return new NotificationEvent(notificationItem.getUserId(), notificationItem.getEventType(),
        notificationItem.getActorId(), Instant.parse(notificationItem.getTimestamp()),
        notificationItem.getPayload(), Instant.ofEpochSecond(notificationItem.getTtl()));
  }
}
