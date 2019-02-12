package com.melardev.spring.shoppingcartweb.models;

import javax.persistence.*;

@Entity
@DiscriminatorValue("1")
public class CategoryImage extends FileUpload {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    Category category;

    boolean isFeaturedImage;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isFeaturedImage() {
        return isFeaturedImage;
    }

    public void setFeaturedImage(boolean featuredImage) {
        isFeaturedImage = featuredImage;
    }


}
