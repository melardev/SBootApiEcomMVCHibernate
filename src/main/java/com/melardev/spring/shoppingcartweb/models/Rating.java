package com.melardev.spring.shoppingcartweb.models;


import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Entity
@Table(name = "ratings")
public class Rating extends TimestampedEntity {

    @Min(1)
    @Max(5)
    private long rating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id") // not required
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id") // not required
    private Product product;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(long rating) {
        this.rating = rating;
    }
}
