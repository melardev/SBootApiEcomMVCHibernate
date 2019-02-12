package com.melardev.spring.shoppingcartweb.models;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("0")
public class ProductImage extends FileUpload {

    @ManyToOne(fetch = FetchType.LAZY)
    Product product;



    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }


}
