package com.notificationservice.dto;

import lombok.Data;

@Data
public class OrderEvent {
    private Long orderId;
    private String description;
    private String status;
}