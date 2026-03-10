package com.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class NotificationService {

    // Хранилище уведомлений: key = id уведомления, value = само уведомление
    private final Map<Long, NotificationRecord> notifications = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Внутренний класс для хранения данных уведомления
    public static class NotificationRecord {
        private final Long id;
        private final Long orderId;
        private final String message;
        private final String timestamp;

        public NotificationRecord(Long id, Long orderId, String message) {
            this.id = id;
            this.orderId = orderId;
            this.message = message;
            this.timestamp = java.time.LocalDateTime.now().toString();
        }

        public Long getId() {
            return id;
        }

        public Long getOrderId() {
            return orderId;
        }

        public String getMessage() {
            return message;
        }

        public String getTimestamp() {
            return timestamp;
        }

        @Override
        public String toString() {
            return String.format("Notification{id=%d, orderId=%d, message='%s', timestamp='%s'}",
                    id, orderId, message, timestamp);
        }
    }

    public NotificationRecord processNotification(Long orderId, String message) {
        // Создаем запись об уведомлении
        Long notificationId = idGenerator.getAndIncrement();
        NotificationRecord record = new NotificationRecord(notificationId, orderId, message);

        // Сохраняем в Map
        notifications.put(notificationId, record);

        // Логируем
        log.info("✅ Уведомление сохранено: {}", record);

        return record;
    }

    // Получить все уведомления
    public Map<Long, NotificationRecord> getAllNotifications() {
        return new ConcurrentHashMap<>(notifications);
    }

    // Получить уведомление по ID
    public NotificationRecord getNotificationById(Long id) {
        return notifications.get(id);
    }

    // Получить уведомления по orderId
    public Map<Long, NotificationRecord> getNotificationsByOrderId(Long orderId) {
        Map<Long, NotificationRecord> result = new ConcurrentHashMap<>();
        notifications.forEach((id, record) -> {
            if (record.getOrderId().equals(orderId)) {
                result.put(id, record);
            }
        });
        return result;
    }

    // Получить количество уведомлений
    public int getNotificationCount() {
        return notifications.size();
    }

    // Очистить все уведомления (для тестирования)
    public void clearAllNotifications() {
        notifications.clear();
        idGenerator.set(1);
        log.info("🧹 Все уведомления очищены");
    }
}