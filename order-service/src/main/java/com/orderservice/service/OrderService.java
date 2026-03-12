package com.orderservice.service;


import com.orderservice.client.NotificationClient;
import com.orderservice.model.Order;
import com.orderservice.reposotory.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final NotificationClient notificationClient;

    public Order createOrder(Order order) {
        // Сохраняем заказ
        Order savedOrder = orderRepository.save(order);
        log.info("Заказ сохранён: {}", savedOrder);

        // Отправляем уведомление (синхронно)
        notificationClient.sendNotification(savedOrder);

        return savedOrder;
    }

    public Map <Long, Order> getAllOrders() {
        return orderRepository.getStorage();
    }
}