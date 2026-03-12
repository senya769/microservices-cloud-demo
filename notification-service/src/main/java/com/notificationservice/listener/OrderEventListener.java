package com.notificationservice.listener;

import com.notificationservice.dto.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Slf4j
@Configuration
public class OrderEventListener {

    @Bean
    public Consumer<OrderEvent> orderCreated() {
        return event -> {
            log.info("Получено событие из RabbitMQ: {}", event);

            // Здесь твоя логика отправки уведомления
            // Например, позвонить в сервис email или смс
            sendNotification(event);
        };
    }

    private void sendNotification(OrderEvent event) {
        log.info("*** УВЕДОМЛЕНИЕ *** Заказ {} для пользователя {} создан. Статус: {}",
                event.getOrderId(), event.getStatus());
    }
}