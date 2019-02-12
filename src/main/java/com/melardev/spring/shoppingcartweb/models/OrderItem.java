package com.melardev.spring.shoppingcartweb.models;

import javax.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem extends TimestampedEntity {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // One order item points to one product, one product points to many order items(may be ordered by many different users)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private String slug;
    private String productName;
    private double price;
    private int quantity;

    public OrderItem(Order order, Product product, int quantity, double price) {
        this.order = order;
        this.product = product;
        this.slug = product.getSlug();
        this.productName = product.getName();
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItem() {
    }

    public Order getOrder() {
        return order;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
