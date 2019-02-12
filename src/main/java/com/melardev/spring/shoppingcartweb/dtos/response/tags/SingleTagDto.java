package com.melardev.spring.shoppingcartweb.dtos.response.tags;

import com.melardev.spring.shoppingcartweb.dtos.response.base.AppResponse;
import com.melardev.spring.shoppingcartweb.models.FileUpload;
import com.melardev.spring.shoppingcartweb.models.Tag;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

public class SingleTagDto extends AppResponse {

    private final Long id;
    @Size(min = 2, max = 25)
    @NotNull(message = "{errors.tag.name.null}")
    @NotEmpty(message = "{errors.tag.name.empty}")
    private String name;

    @NotNull
    @Size(min = 2, max = 100)
    @NotEmpty(message = "{errors.tag.content.empty}")
    private String description;

    private List<String> imageUrls;

    public SingleTagDto(Long id, String name, String description, List<String> imageUrls) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageUrls = imageUrls;
    }

    public static SingleTagDto build(Tag tag) {
        return build(tag, false, false);
    }

    public static SingleTagDto build(Tag tag, boolean includeDescription, boolean includeUrls) {
        List<String> urls = null;
        if (includeUrls)
            urls = tag.getImages().stream().map(FileUpload::getUrlPath).collect(Collectors.toList());
        return new SingleTagDto(tag.getId(), tag.getName(),
                includeDescription ? tag.getDescription() : null, urls);
    }

    public static SingleTagDto build(Tag tag, boolean includeUrls) {
        return build(tag, false, includeUrls);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}