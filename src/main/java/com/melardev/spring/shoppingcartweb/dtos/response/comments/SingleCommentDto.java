package com.melardev.spring.shoppingcartweb.dtos.response.comments;


import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.users.partials.UserIdAndUsernameDto;
import com.melardev.spring.shoppingcartweb.models.Comment;

import java.time.ZonedDateTime;

public class SingleCommentDto extends SuccessResponse {
    private final ZonedDateTime updatedAt;
    private final ZonedDateTime createdAt;
    private final Long productId;

    private final UserIdAndUsernameDto user;
    private Long id;

    private String content;


    private SingleCommentDto(Long id, String content, ZonedDateTime createdAt, ZonedDateTime updatedAt, Long productId,

                             UserIdAndUsernameDto user) {
        this.id = id;
        this.content = content;
        this.productId = productId;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SingleCommentDto build(Comment comment) {
        // TODO: why getId() is always null, but .id is real id.
        return new SingleCommentDto(comment.id, comment.getContent(), comment.getCreatedAt(), comment.getUpdatedAt(),
                comment.getProduct().getId(),
                UserIdAndUsernameDto.build(comment.getUser()));
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public Long getProductId() {
        return productId;
    }

    public UserIdAndUsernameDto getUser() {
        return user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
