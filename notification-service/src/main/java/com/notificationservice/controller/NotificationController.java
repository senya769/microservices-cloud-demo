package com.notificationservice.controller;

import com.notificationservice.service.NotificationService;
import com.notificationservice.service.NotificationService.NotificationRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<Map<String, Object>> receiveNotification(@RequestBody Map<String, Object> payload) {
        try {
            log.debug("Получен запрос с payload: {}", payload);

            Long orderId = Long.valueOf(payload.get("orderId").toString());
            String message = (String) payload.get("message");

            NotificationRecord savedNotification = notificationService.processNotification(orderId, message);

            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Notification processed successfully");
            response.put("notificationId", savedNotification.getId());
            response.put("orderId", savedNotification.getOrderId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Ошибка при обработке уведомления: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Error processing notification: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllNotifications() {
        Map<Long, NotificationRecord> notifications = notificationService.getAllNotifications();

        Map<String, Object> response = new HashMap<>();
        response.put("count", notifications.size());
        response.put("notifications", notifications.values());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long id) {
        NotificationRecord notification = notificationService.getNotificationById(id);

        if (notification == null) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Notification not found with id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(notification);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<Map<String, Object>> getNotificationsByOrderId(@PathVariable Long orderId) {
        Map<Long, NotificationRecord> notifications = notificationService.getNotificationsByOrderId(orderId);

        Map<String, Object> response = new HashMap<>();
        response.put("orderId", orderId);
        response.put("count", notifications.size());
        response.put("notifications", notifications.values());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Integer>> getNotificationCount() {
        Map<String, Integer> response = new HashMap<>();
        response.put("count", notificationService.getNotificationCount());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearAllNotifications() {
        notificationService.clearAllNotifications();

        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "All notifications cleared");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("service", "notification-service");
        status.put("status", "UP");
        status.put("timestamp", java.time.LocalDateTime.now().toString());
        return ResponseEntity.ok(status);
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "notification-service");
        info.put("status", "UP");
        info.put("version", "1.0.0");
        info.put("notificationsCount", notificationService.getNotificationCount());
        return ResponseEntity.ok(info);
    }
}