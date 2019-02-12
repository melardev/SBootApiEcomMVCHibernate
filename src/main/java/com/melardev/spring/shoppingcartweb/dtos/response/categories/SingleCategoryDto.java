package com.melardev.spring.shoppingcartweb.dtos.response.categories;

import com.melardev.spring.shoppingcartweb.dtos.response.base.AppResponse;
import com.melardev.spring.shoppingcartweb.models.Category;
import com.melardev.spring.shoppingcartweb.models.FileUpload;

import java.util.List;
import java.util.stream.Collectors;

public class SingleCategoryDto extends AppResponse {


    private Long id;

    private String name;

    private String description;

    private List<String> imageUrls;

    public SingleCategoryDto(Long id, String name, String description, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrls = imageUrls;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public static SingleCategoryDto build(Category category) {
        return build(category, false, false);
    }

    public static SingleCategoryDto build(Category category, boolean includeDescription, boolean includeUrls) {
        List<String> urls = null;
        if (includeUrls)
            urls = category.getImages().stream().map(FileUpload::getFilePath).collect(Collectors.toList());

        return new SingleCategoryDto(category.getId(), category.getName(),
                includeDescription ? category.getDescription() : null,
                urls);
    }

    public static SingleCategoryDto build(Category category, boolean includeDescription) {
        return build(category, includeDescription, false);
    }
}