package com.melardev.spring.shoppingcartweb.dtos.request.products;

import com.melardev.spring.shoppingcartweb.dtos.request.images.ImageDto;
import com.melardev.spring.shoppingcartweb.dtos.response.categories.SingleCategoryDto;
import com.melardev.spring.shoppingcartweb.dtos.response.tags.SingleTagDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

public class CreateOrEditProductDto {

    @NotEmpty
    @NotNull
    private String name;

    @NotEmpty
    @NotNull
    private String description;

    private Set<SingleTagDto> tags;

    //@NotNull Not implemented yet
    private Set<SingleCategoryDto> categories;

    private Set<ImageDto> images;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<SingleTagDto> getTags() {
        return tags;
    }

    public void setTags(Set<SingleTagDto> tags) {
        this.tags = tags;
    }

    public Set<SingleCategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(Set<SingleCategoryDto> categories) {
        this.categories = categories;
    }

    public Set<ImageDto> getImages() {
        return images;
    }

    public void setImages(Set<ImageDto> images) {
        this.images = images;
    }
}
