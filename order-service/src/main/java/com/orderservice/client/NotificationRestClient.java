package com.orderservice.client;

import com.orderservice.model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationRestClient implements NotificationClient {

    private final WebClient.Builder webClientBuilder;

    @Override
    public void sendNotification(Order order) {
        WebClient webClient = webClientBuilder
                .baseUrl("http://notification-service")  // имя, под которым сервис зарегистрирован в Eureka
                .build();

        NotificationRequest request = new NotificationRequest(
                order.getId(),
                "Создан новый заказ: " + order.getDescription()
        );

        webClient.post()
                .uri("/api/notifications")   // относительный путь
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();  // пока оставим .block(), чтобы было синхронно

        log.info("Уведомление отправлено");
    }

    private record NotificationRequest(Long orderId, String message) {
    }
}