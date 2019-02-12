package com.melardev.spring.shoppingcartweb.dtos.response.orders.partials;

import com.melardev.spring.shoppingcartweb.models.OrderItem;

public class OrderItemDto {
    private final Long id;
    private final String name;
    private final String slug;
    private final double price;
    private final int quantity;

    private OrderItemDto(Long id, String name, String slug, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.quantity = quantity;
    }

    public static OrderItemDto build(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getId(),
                orderItem.getProduct().getName(),
                orderItem.getProduct().getSlug(),
                orderItem.getProduct().getPrice(),
                orderItem.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
