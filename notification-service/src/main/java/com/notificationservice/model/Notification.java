package com.notificationservice.model;

import lombok.Data;

@Data
public class Notification {
    private Long id;          // можно сгенерировать, но для учебных целей необязательно
    private Long orderId;
    private String message;
}