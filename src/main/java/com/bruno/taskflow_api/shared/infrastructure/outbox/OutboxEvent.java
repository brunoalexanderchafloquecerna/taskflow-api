package com.bruno.taskflow_api.shared.infrastructure.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Entity
@Table(name = "outbox_event")
public class OutboxEvent {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "aggregate_type", nullable = false)
  private String aggregateType;

  @Column(name = "aggregate_id", nullable = false)
  private UUID aggregateId;

  @Column(name = "event_type", nullable = false)
  private String eventType;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "payload", columnDefinition = "jsonb", nullable = false)
  private String payload;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  @Column(name = "processed_at")
  private Instant processedAt;

  protected OutboxEvent() {
  }

  public static OutboxEvent of(String aggregateType, UUID aggregateId, String eventType,
      String payload) {
    OutboxEvent outboxEvent = new OutboxEvent();
    outboxEvent.aggregateType = aggregateType;
    outboxEvent.aggregateId = aggregateId;
    outboxEvent.eventType = eventType;
    outboxEvent.payload = payload;
    outboxEvent.createdAt = Instant.now();
    return outboxEvent;
  }

  public void markAsProcessed() {
    this.processedAt = Instant.now();
  }
}
