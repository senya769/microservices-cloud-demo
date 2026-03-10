package com.orderservice.client;

import com.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRestClient implements NotificationClient {

    private final RestTemplate restTemplate;

    @Value("${notification.service.url:http://localhost:8082}")
    private String notificationServiceUrl;

    @Override
    public void sendNotification(Order order) {
        String url = notificationServiceUrl + "/api/notifications";

        // Используем HashMap вместо Map.of для избежания проблем с неизменяемостью
        Map<String, Object> request = new HashMap<>();
        request.put("orderId", order.getId());
        request.put("message", "Создан новый заказ: " + order.getDescription());

        try {
            restTemplate.postForEntity(url, request, Void.class);
            log.info("✅ Уведомление отправлено в Notification Service для заказа ID: {}", order.getId());
        } catch (Exception e) {
            log.error("❌ Ошибка при отправке уведомления: {}", e.getMessage());
        }
    }
}