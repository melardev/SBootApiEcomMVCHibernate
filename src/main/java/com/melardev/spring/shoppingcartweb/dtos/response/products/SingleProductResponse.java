package com.melardev.spring.shoppingcartweb.dtos.response.products;

import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.categories.SingleCategoryDto;
import com.melardev.spring.shoppingcartweb.dtos.response.comments.partials.CommentPartialDto;
import com.melardev.spring.shoppingcartweb.dtos.response.tags.SingleTagDto;
import com.melardev.spring.shoppingcartweb.models.Comment;
import com.melardev.spring.shoppingcartweb.models.Product;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class SingleProductResponse extends SuccessResponse {


    private final List<CommentPartialDto> comments;
    private final List<String> images;
    public long id;
    public String name;
    public String slug;
    private String description;
    private double price;

    private List<SingleTagDto> tags;

    private List<SingleCategoryDto> categories;

    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    private AuthInfo authInfo;

    public SingleProductResponse(Long id, String name, String description, String slug, double price,
                                 List<CommentPartialDto> commentPartialDtos, List<SingleTagDto> tagDtos, List<SingleCategoryDto> categoryDtos, List<String> imageList) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.slug = slug;
        this.price = price;
        this.comments = commentPartialDtos;
        this.tags = tagDtos;
        this.categories = categoryDtos;
        this.images = imageList;
    }

    public static SingleProductResponse build(Product product) {
        List<CommentPartialDto> commentPartialDtos = new ArrayList<>();
        for (Comment comment : product.getComments()) {
            commentPartialDtos.add(CommentPartialDto.build(comment, true));
        }
        if (product.getTags() == null)
            product.setTags(new HashSet<>(0));
        List<SingleTagDto> tagNamesList = product.getTags().stream().map(SingleTagDto::build).collect(Collectors.toList());

        if (product.getCategories() == null)
            product.setCategories(new HashSet<>(0));
        List<SingleCategoryDto> categoryList = product.getCategories().stream().map(c -> SingleCategoryDto.build(c, false, false)).collect(Collectors.toList());

        if (product.getImages() == null)
            product.setImages(new HashSet<>(0));
        List<String> imageList = product.getImages().stream().map(image -> image.getUrlPath().replace("\\", "/")).collect(Collectors.toList());

        return new SingleProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getSlug(),
                product.getPrice(),
                commentPartialDtos,
                tagNamesList,
                categoryList,
                imageList
        );
    }

    public List<CommentPartialDto> getComments() {
        return comments;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<SingleTagDto> getTags() {
        return tags;
    }

    public void setTags(List<SingleTagDto> tags) {
        this.tags = tags;
    }

    public List<SingleCategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(List<SingleCategoryDto> categories) {
        this.categories = categories;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AuthInfo getAuthInfo() {
        return authInfo;
    }

    public void setAuthInfo(AuthInfo authInfo) {
        this.authInfo = authInfo;
    }


    public static class AuthInfo {
        private boolean canEdit;
        private boolean canDelete;

        public void setCanEdit(boolean canEdit) {
            this.canEdit = canEdit;
        }

        public boolean getCanEdit() {
            return canEdit;
        }

        public void setCanDelete(boolean canDelete) {
            this.canDelete = canDelete;
        }

        public boolean getCanDelete() {
            return canDelete;
        }
    }

    public List<String> getImages() {
        return images;
    }
}
