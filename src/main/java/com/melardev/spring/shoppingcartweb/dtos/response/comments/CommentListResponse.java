package com.melardev.spring.shoppingcartweb.dtos.response.comments;

import com.melardev.spring.shoppingcartweb.dtos.response.comments.partials.CommentPartialDto;
import com.melardev.spring.shoppingcartweb.dtos.response.shared.PageMeta;
import com.melardev.spring.shoppingcartweb.models.Comment;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommentListResponse {

    private PageMeta pageMeta;
    private Collection<CommentPartialDto> comments;

    public CommentListResponse() {
        comments = new ArrayList<>();
    }

    private CommentListResponse(PageMeta pageMeta, List<CommentPartialDto> commentPartialDtos) {
        this.pageMeta = pageMeta;
        this.comments = commentPartialDtos;
    }

    public static CommentListResponse build(Page<Comment> commentsPage, String basePath) {
        List<CommentPartialDto> commentPartialDtos = new ArrayList<>();
        for (Comment comment : commentsPage.getContent()) {
            commentPartialDtos.add(CommentPartialDto.build(comment));
        }

        return new CommentListResponse(
                PageMeta.build(commentsPage, basePath),
                commentPartialDtos
        );
    }

    public PageMeta getPageMeta() {
        return pageMeta;
    }

    public void setPageMeta(PageMeta pageMeta) {
        this.pageMeta = pageMeta;
    }

    public Collection<CommentPartialDto> getComments() {
        return comments;
    }

}
