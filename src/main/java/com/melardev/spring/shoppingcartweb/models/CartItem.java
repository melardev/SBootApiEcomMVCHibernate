package com.melardev.spring.shoppingcartweb.models;

public class CartItem {
    private int quantity;
    private Product product;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalAmount() {
        return getQuantity() * getPrice();
    }

    private double getPrice() {
        return product.getPrice();
    }
}
