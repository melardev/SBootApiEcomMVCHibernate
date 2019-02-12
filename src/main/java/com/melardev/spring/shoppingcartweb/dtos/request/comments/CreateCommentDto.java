package com.melardev.spring.shoppingcartweb.dtos.request.comments;

import com.melardev.spring.shoppingcartweb.models.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

public class CreateCommentDto {

    //@Size(min = 2, max = 300, message = "{errors.comment.content.size}")
    //@NotNull(message = "{errors.comment.content.null}")
    //@NotEmpty(message = "{errors.comment.content.empty}")
    @NotNull
    @NotEmpty
    public String content;
    public User user;

    private Long id;

    public Long getId() {
        return id;
    }

    public CreateCommentDto() {
    }

    public void setId(Long id) {
        this.id = id;
    }

    private ZonedDateTime createdAt;


    private ZonedDateTime updatedAt;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}