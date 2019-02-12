package com.melardev.spring.shoppingcartweb.models;

import javax.persistence.*;

@Entity
@DiscriminatorValue("2")
public class TagImage extends FileUpload {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tag_id")
    Tag tag;

    boolean isFeaturedImage;

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public boolean isFeaturedImage() {
        return isFeaturedImage;
    }

    public void setFeaturedImage(boolean featuredImage) {
        isFeaturedImage = featuredImage;
    }
}
