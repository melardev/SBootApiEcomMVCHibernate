package com.melardev.spring.shoppingcartweb.dtos.request;

import com.melardev.spring.shoppingcartweb.dtos.response.categories.SingleCategoryDto;
import com.melardev.spring.shoppingcartweb.dtos.response.tags.SingleTagDto;

public class ProductCreateDto {

    String slug;
    Double price;
    SingleCategoryDto[] categories;
    SingleTagDto[] tags;


    String name;
    String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public SingleCategoryDto[] getCategories() {
        return categories;
    }

    public void setCategories(SingleCategoryDto[] categories) {
        this.categories = categories;
    }

    public SingleTagDto[] getTags() {
        return tags;
    }

    public void setTags(SingleTagDto[] tags) {
        this.tags = tags;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
