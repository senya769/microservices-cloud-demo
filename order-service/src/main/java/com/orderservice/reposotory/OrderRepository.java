package com.orderservice.reposotory;

import com.orderservice.model.Order;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class OrderRepository {
    private final Map<Long, Order> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Order save(Order order) {
        long id = idGenerator.getAndIncrement();
        order.setId(id);
        order.setStatus("CREATED");
        storage.put(id, order);
        return order;
    }
}