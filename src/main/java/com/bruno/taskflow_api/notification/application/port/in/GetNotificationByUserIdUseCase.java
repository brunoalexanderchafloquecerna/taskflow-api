package com.bruno.taskflow_api.notification.application.port.in;

import com.bruno.taskflow_api.notification.domain.model.NotificationEvent;
import java.util.List;

public interface GetNotificationByUserIdUseCase {

  List<NotificationEvent> findByUserId(String userId);
}
