package com.melardev.spring.shoppingcartweb.models.extensions;

import com.melardev.spring.shoppingcartweb.models.Tag;

public class TagExtension extends Tag {

    Long productId;
    public TagExtension(Long id, String name, Long productId) {
        this.name = name;
        this.id = id;
        this.productId=productId;
    }

    public Long getProductId() {
        return productId;
    }
}
