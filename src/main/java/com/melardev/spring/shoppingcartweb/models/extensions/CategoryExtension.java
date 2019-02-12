package com.melardev.spring.shoppingcartweb.models.extensions;

import com.melardev.spring.shoppingcartweb.models.Category;

public class CategoryExtension extends Category {

    Long productId;

    public CategoryExtension(Long id, String name, Long productId) {
        this.name = name;
        this.id = id;
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
