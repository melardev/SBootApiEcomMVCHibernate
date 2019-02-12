package com.melardev.spring.shoppingcartweb.dtos.request.cart;

public class CartItemDto {

    public CartItemDto() {
    }

    public CartItemDto(Long id, String name, String slug, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    private Long id;
    private String name;
    private double price;
    private Integer quantity;
    private String slug;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}
