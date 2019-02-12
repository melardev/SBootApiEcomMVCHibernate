package com.melardev.spring.shoppingcartweb.models.extensions;

import com.melardev.spring.shoppingcartweb.models.OrderItem;

public class OrderItemExtension extends OrderItem {
    private Long orderId;

    public Long getOrderId() {
        return orderId;
    }
}
