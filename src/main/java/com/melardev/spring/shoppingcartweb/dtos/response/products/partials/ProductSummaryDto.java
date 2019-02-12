package com.melardev.spring.shoppingcartweb.dtos.response.products.partials;


import com.melardev.spring.shoppingcartweb.dtos.response.categories.SingleCategoryDto;
import com.melardev.spring.shoppingcartweb.dtos.response.tags.SingleTagDto;
import com.melardev.spring.shoppingcartweb.models.Product;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;


public class ProductSummaryDto {


    private List<String> imageUrls;
    private Long commentsCount;
    private List<SingleCategoryDto> categories;
    private List<SingleTagDto> tags;
    private Long id;
    private String slug;
    private double price;
    private int stock;

    public String name;

    private String description;

    private ZonedDateTime createdAt;

    private ZonedDateTime updatedAt;

    private ProductSummaryDto(Long id, String name, String slug, int stock, double price,
                              List<SingleTagDto> tags,
                              List<SingleCategoryDto> categories, Long commentsCount, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.stock = stock;
        this.price = price;
        this.tags = tags;
        this.categories = categories;
        this.commentsCount = commentsCount;
        this.imageUrls = imageUrls;
    }

    public ProductSummaryDto() {

    }


    public List<SingleCategoryDto> getCategories() {
        return categories;
    }

    public List<SingleTagDto> getTags() {
        return tags;
    }

    public static ProductSummaryDto build(Product product) {

        return new ProductSummaryDto(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getStock(),
                product.getPrice(),
                product.getTags() != null ? product.getTags().stream().map(SingleTagDto::build).collect(Collectors.toList()) : null,
                product.getCategories() != null ? product.getCategories().stream().map(SingleCategoryDto::build).collect(Collectors.toList()) : null,
                product.getCommentCount(),
                product.getImages() != null ? product.getImages().stream().map(ti -> ti.getUrlPath()).collect(Collectors.toList()) : null
        );
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCommentsCount() {
        return commentsCount;
    }

    public int getStock() {
        return stock;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
