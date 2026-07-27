package com.bruno.taskflow_api.task.infrastructure.adapter.out.persistence.dynamodb;

import com.bruno.taskflow_api.task.application.port.out.ActivityLogPort;
import com.bruno.taskflow_api.task.domain.model.ActivityEvent;
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
public class DynamoDbActivityLogAdapter implements ActivityLogPort {

  private final DynamoDbTable<ActivityLogItem> table;

  public DynamoDbActivityLogAdapter(DynamoDbEnhancedClient enhancedClient,
      @Value("${aws.dynamodb.table-name}") String tableName) {
    this.table = enhancedClient.table(tableName, TableSchema.fromBean(ActivityLogItem.class));
  }

  @Override
  public void record(ActivityEvent event) {
    table.putItem(toItem(event));
  }

  @Override
  public List<ActivityEvent> findByTaskId(String taskId) {
    QueryConditional queryConditional = QueryConditional.keyEqualTo(
        Key.builder().partitionValue(ActivityLogKeys.buildPk(taskId)).build());

    return table.query(queryConditional).items().stream().map(this::toDomain).toList();
  }

  private ActivityLogItem toItem(ActivityEvent event) {
    ActivityLogItem item = new ActivityLogItem();
    item.setPk(ActivityLogKeys.buildPk(event.taskId()));
    item.setSk(ActivityLogKeys.buildSk(event.timestamp()));
    item.setTaskId(event.taskId());
    item.setEventType(event.eventType());
    item.setActorId(event.actorId());
    item.setTimestamp(event.timestamp().toString());
    item.setPayload(event.payload());
    return item;
  }

  private ActivityEvent toDomain(ActivityLogItem item) {
    return new ActivityEvent(item.getTaskId(), item.getEventType(), item.getActorId(),
        Instant.parse(item.getTimestamp()), item.getPayload());
  }
}