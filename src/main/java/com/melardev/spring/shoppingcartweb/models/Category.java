package com.melardev.spring.shoppingcartweb.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "categories")
public class Category extends TimestampedEntity {

    @Column(nullable = false, unique = true)
    protected String name;

    private String description;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "categories")
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<CategoryImage> images = new ArrayList<>();

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category() {

    }

    public Category(String name) {
        this.setName(name);
    }

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public List<CategoryImage> getImages() {
        return images;
    }

    public void setImages(List<CategoryImage> images) {
        this.images=images;
    }
}
