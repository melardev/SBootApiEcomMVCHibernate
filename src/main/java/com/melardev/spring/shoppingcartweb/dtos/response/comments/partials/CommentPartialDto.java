package com.melardev.spring.shoppingcartweb.dtos.response.comments.partials;

import com.melardev.spring.shoppingcartweb.dtos.response.base.SuccessResponse;
import com.melardev.spring.shoppingcartweb.dtos.response.users.partials.UserIdAndUsernameDto;
import com.melardev.spring.shoppingcartweb.models.Comment;

import java.time.ZonedDateTime;

public class CommentPartialDto extends SuccessResponse {

    private final ZonedDateTime updatedAt;
    private final ZonedDateTime createdAt;
    private final Long productId;

    private final UserIdAndUsernameDto user;
    private Long id;

    private String content;


    public CommentPartialDto(Long id, String content, ZonedDateTime createdAt, ZonedDateTime updatedAt, Long productId,

                             UserIdAndUsernameDto user) {
        this.id = id;
        this.content = content;
        this.productId = productId;

        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CommentPartialDto build(Comment comment) {
        return build(comment, false);
    }

    public static CommentPartialDto build(Comment comment, boolean includeUser) {
        return new CommentPartialDto(
                comment.id, comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getProduct().getId(),
                includeUser ? UserIdAndUsernameDto.build(comment.getUser()) : null
        );
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
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

    public Long getProductId() {
        return productId;
    }


    public UserIdAndUsernameDto getUser() {
        return user;
    }
}

