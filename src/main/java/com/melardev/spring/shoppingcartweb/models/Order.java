package com.melardev.spring.shoppingcartweb.models;

import com.melardev.spring.shoppingcartweb.enums.OrderStatus;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends TimestampedEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    String trackingNumber;

    @Enumerated
    OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Address address;

    // TODO: think more about consequence of cascade remove ... not sure
    // It is mapped by OrderItem:: "order"
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    Collection<OrderItem> orderItems;

    // @Type(type = "org.hibernate.type.NumericBooleanType")
    // @Column(name = "delivered", nullable = false)
    // private Boolean delivered = false;

    @Transient
    double total;

    @Transient
    private Long orderItemsCount;

    public Order(Collection<OrderItem> orderItems, User user, String address, String city, String country, Integer zipCode) {
        this.orderItems = orderItems;
        this.user = user;
    }

    /**
     * Warning, this may cause issues
     */
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "order_items",
            joinColumns = {@JoinColumn(name = "order_id", insertable = false, updatable = false)},
            inverseJoinColumns = {@JoinColumn(name = "user_id", insertable = false, updatable = false)})
    private User user;*/
    public Order() {

    }

    public Order(List<OrderItem> orderItems, Address address, User user) {

        this.address = address;
        this.orderItems = orderItems;
        this.user = user;
    }

    public Collection<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Collection<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public User getUser() {
        if (user == null)
            return new User();
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotalAmount() {
        if (total == 0) {
            total = orderItems.stream().mapToDouble(orderItem -> {
                return orderItem.getQuantity() * orderItem.getProduct().getPrice();
            }).sum();
        }
        return total;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public double getTotal() {
        return total;
    }

    public void setOrderItemsCount(Long orderItemsCount) {
        this.orderItemsCount = orderItemsCount;
    }

    public Long getOrderItemsCount() {
        return orderItemsCount;
    }
}
