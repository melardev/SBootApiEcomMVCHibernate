package com.melardev.spring.shoppingcartweb.models;

import javax.persistence.*;

@Table(name = "comments")
@Entity
public class Comment extends TimestampedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") // not required
    private User user;

    @ManyToOne
    @JoinColumn(name = "product_id") // not required
    private Product product;

    private Integer rating;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }
}