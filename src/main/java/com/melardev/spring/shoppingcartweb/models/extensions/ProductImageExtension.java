package com.melardev.spring.shoppingcartweb.models.extensions;

import com.melardev.spring.shoppingcartweb.models.ProductImage;

public class ProductImageExtension  extends ProductImage {
    private final String filePath;
    Long productId;

    public ProductImageExtension(Long productId, String filePath) {
        this.productId = productId;
        this.filePath = filePath;

    }

    public Long getProductId() {
        return productId;
    }

    public String getFilePath() {
        return filePath;
    }
}
