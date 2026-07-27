package com.bruno.taskflow_api.notification.infrastructure.adapter.in.web;

import com.bruno.taskflow_api.notification.application.port.in.GetNotificationByUserIdUseCase;
import com.bruno.taskflow_api.notification.domain.model.NotificationEvent;
import com.bruno.taskflow_api.shared.application.port.out.AuthenticatedUserProvider;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class NotificationController {

  private final GetNotificationByUserIdUseCase getNotificationByUserIdUseCase;

  private final AuthenticatedUserProvider authenticatedUserProvider;

  @GetMapping("/notifications")
  public ResponseEntity<List<NotificationEventResponse>> getNotifications() {
    String currentUserId = authenticatedUserProvider.getCurrentUserId().toString();
    return ResponseEntity.ok(
        getNotificationByUserIdUseCase.findByUserId(currentUserId).stream().map(this::toResponse)
            .toList());
  }

  private NotificationEventResponse toResponse(NotificationEvent notificationEvent) {
    return new NotificationEventResponse(notificationEvent.userId(), notificationEvent.eventType(),
        notificationEvent.actorId(), notificationEvent.timestamp(), notificationEvent.payload(),
        notificationEvent.expiresAt());
  }
}
