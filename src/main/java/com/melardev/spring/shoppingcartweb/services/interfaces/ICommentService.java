package com.melardev.spring.shoppingcartweb.services.interfaces;

import com.melardev.spring.shoppingcartweb.models.Comment;
import com.melardev.spring.shoppingcartweb.models.Product;
import com.melardev.spring.shoppingcartweb.models.User;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICommentService extends CrudService<Comment> {
    Page<Comment> getCommentsFromProductWithSlug(String slug, int page, int count);

    Comment findById(Long id);

    Page<Comment> getCommentsFromUserWithId(Long id, int page, int count);

    Comment findByIdNotThrow(Long id);

    Comment save(String content, Product product, User user);

    Comment update(Long id, String content, User user);

    Comment update(Comment comment, String content, User user);

    long getCount();

    List<Object[]> getCommentCountForProductIds(List<Long> productIds);
}
