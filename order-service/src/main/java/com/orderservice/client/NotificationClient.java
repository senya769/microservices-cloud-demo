package com.orderservice.client;

import com.orderservice.model.Order;

public interface NotificationClient {
    void sendNotification(Order order);
}