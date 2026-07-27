package com.bruno.taskflow_api.notification.application.port.out;

import com.bruno.taskflow_api.notification.domain.model.NotificationEvent;
import java.util.List;

public interface NotificationRepository {

  void record(NotificationEvent event);

  List<NotificationEvent> findByUserId(String userId);
}
