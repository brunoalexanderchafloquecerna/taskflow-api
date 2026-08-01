package com.bruno.taskflow_api.task.infrastructure.adapter.out.event;

import com.bruno.taskflow_api.shared.infrastructure.outbox.OutboxEventWriter;
import com.bruno.taskflow_api.task.application.event.TaskCompletedIntegrationEvent;
import com.bruno.taskflow_api.task.application.port.out.TaskEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class OutboxTaskEventPublisherAdapter implements TaskEventPublisher {

  private final OutboxEventWriter outboxEventWriter;

  public OutboxTaskEventPublisherAdapter(OutboxEventWriter outboxEventWriter) {
    this.outboxEventWriter = outboxEventWriter;
  }

  @Override
  public void publishTaskCompleted(TaskCompletedIntegrationEvent event) {
    outboxEventWriter.write("TASK", event.taskId(), "TASK_COMPLETED", event);
  }
}
