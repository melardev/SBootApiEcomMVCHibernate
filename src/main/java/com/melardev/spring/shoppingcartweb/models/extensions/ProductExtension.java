package com.melardev.spring.shoppingcartweb.models.extensions;

import com.melardev.spring.shoppingcartweb.models.Product;

import java.time.ZonedDateTime;

public class ProductExtension extends Product {

    public ProductExtension(Long id, double price) {
        this.id = id;
        this.price = price;
    }

    public ProductExtension(Long id, String name, String slug, double price) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.price = price;
    }


    public ProductExtension(Long id, String name, String slug, double price, int stock, ZonedDateTime publishOn) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.slug = slug;
        this.stock = stock;
        this.publishOn = publishOn;
    }


    public ProductExtension(Long id, String name, String slug, String description, double price, int stock, ZonedDateTime publishOn) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.price = price;
        this.stock = stock;
        this.publishOn = publishOn;
    }
}
