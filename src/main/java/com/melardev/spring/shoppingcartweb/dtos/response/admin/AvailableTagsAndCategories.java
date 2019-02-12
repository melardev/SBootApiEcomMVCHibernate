package com.melardev.spring.shoppingcartweb.dtos.response.admin;

import com.melardev.spring.shoppingcartweb.dtos.response.categories.SingleCategoryDto;
import com.melardev.spring.shoppingcartweb.dtos.response.tags.SingleTagDto;
import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;

import java.util.Collection;

public class AvailableTagsAndCategories extends SuccessResponse {
    private Collection<SingleTagDto> tags;

    //@NotNull Not implemented yet
    private Collection<SingleCategoryDto> categories;

    public Collection<SingleTagDto> getTags() {
        return tags;
    }

    public void setTags(Collection<SingleTagDto> tags) {
        this.tags = tags;
    }

    public Collection<SingleCategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(Collection<SingleCategoryDto> categories) {
        this.categories = categories;
    }
}
