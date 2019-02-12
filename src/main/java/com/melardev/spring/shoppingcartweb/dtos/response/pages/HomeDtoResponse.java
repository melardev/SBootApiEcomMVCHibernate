package com.melardev.spring.shoppingcartweb.dtos.response.pages;

import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.categories.SingleCategoryDto;
import com.melardev.spring.shoppingcartweb.dtos.response.tags.SingleTagDto;
import com.melardev.spring.shoppingcartweb.models.Category;
import com.melardev.spring.shoppingcartweb.models.Tag;

import java.util.ArrayList;
import java.util.List;

public class HomeDtoResponse extends SuccessResponse {
    private final List<SingleTagDto> tags;
    private final List<SingleCategoryDto> categories;

    public HomeDtoResponse(List<SingleTagDto> tagDtos, List<SingleCategoryDto> categoryDtos) {
        this.tags = tagDtos;
        this.categories = categoryDtos;
    }

    public List<SingleTagDto> getTags() {
        return tags;
    }

    public List<SingleCategoryDto> getCategories() {
        return categories;
    }

    public static HomeDtoResponse build(List<Tag> tags, List<Category> categories) {
        List<SingleCategoryDto> categoryDtos = new ArrayList<>(categories.size());
        List<SingleTagDto> tagDtos = new ArrayList<>(tags.size());
        for (Tag tag : tags) {
            tagDtos.add(SingleTagDto.build(tag, true, true));
        }

        for (Category category : categories) {
            categoryDtos.add(SingleCategoryDto.build(category, true, true));
        }

        return new HomeDtoResponse(tagDtos, categoryDtos);
    }
}
