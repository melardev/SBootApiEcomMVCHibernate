package com.melardev.spring.shoppingcartweb.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tags")
public class Tag extends TimestampedEntity {


    @Column(nullable = false, unique = true)
    protected String name;

    private String description;

    // mappedBy indicates this is not the owning side, it is the Product who owns the relationship
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tags")
    private List<Product> products = new ArrayList<>();


    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL)
    private List<TagImage> images = new ArrayList<>();

    public Tag() {

    }

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Tag(String name) {
        this.setName(name);
    }

    public Tag(String name, String description) {
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

    public List<TagImage> getImages() {
        return images;
    }

    public void setImages(List<TagImage> images) {
        this.images = images;
    }
}
