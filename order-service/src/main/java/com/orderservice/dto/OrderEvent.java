package com.orderservice.dto;

import com.orderservice.model.Order;
import lombok.Data;

@Data
public class OrderEvent {
    private Long orderId;
    private String description;
    private String status;

    public static OrderEvent fromOrder(Order order) {
        OrderEvent event = new OrderEvent();
        event.setOrderId(order.getId());
        event.setDescription(order.getDescription());
        event.setStatus(order.getStatus());
        return event;
    }
}