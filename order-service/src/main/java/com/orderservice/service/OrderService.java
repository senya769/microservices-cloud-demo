package com.orderservice.service;


import com.orderservice.dto.OrderEvent;
import com.orderservice.model.Order;
import com.orderservice.reposotory.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final StreamBridge streamBridge; // Внедряем StreamBridge

    public Order createOrder(Order order) {
        // Сохраняем заказ
        Order savedOrder = orderRepository.save(order);
        log.info("Заказ сохранён: {}", savedOrder);

        // Создаём событие
        OrderEvent event = OrderEvent.fromOrder(savedOrder);

        // Отправляем событие в канал "orderCreated"
        boolean sent = streamBridge.send("orderCreated-out-0", event);

        if (sent) {
            log.info("Событие для заказа {} отправлено в RabbitMQ", savedOrder.getId());
        } else {
            log.error("Не удалось отправить событие для заказа {}", savedOrder.getId());
        }

        return savedOrder;
    }

    public Map<Long, Order> getAllOrders() {
        return orderRepository.getStorage();
    }
}