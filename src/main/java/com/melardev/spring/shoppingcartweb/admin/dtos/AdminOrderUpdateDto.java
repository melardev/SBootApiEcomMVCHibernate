package com.melardev.spring.shoppingcartweb.admin.dtos;

import com.melardev.spring.shoppingcartweb.enums.OrderStatus;

public class AdminOrderUpdateDto {

    private String trackingNumber;

    // you can pass either number such as "order_status": 0 or string such as "order_status": "PROCESSED" they both work
    private OrderStatus orderStatus;

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
