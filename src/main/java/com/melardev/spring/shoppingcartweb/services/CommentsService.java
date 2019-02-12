package com.melardev.spring.shoppingcartweb.services;


import com.melardev.spring.shoppingcartweb.errors.exceptions.ResourceNotFoundException;
import com.melardev.spring.shoppingcartweb.models.Comment;
import com.melardev.spring.shoppingcartweb.models.Product;
import com.melardev.spring.shoppingcartweb.models.User;
import com.melardev.spring.shoppingcartweb.repository.CommentsRepository;
import com.melardev.spring.shoppingcartweb.services.interfaces.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CommentsService implements ICommentService {
    private CommentsRepository commentsRepository;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        this.commentsRepository = commentsRepository;
    }


    @Override
    public List<Comment> findAll() {
        return commentsRepository.findAll();
    }

    @Override
    public Page<Comment> findLatest(int page, int count) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.DESC, "createdAt");
        Page<Comment> result = this.commentsRepository.findAll(pageRequest);
        return result;
    }

    @Override
    public long getAllCount() {
        return commentsRepository.count();
    }

    @Override
    public Comment getRandom() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Page<Comment> getCommentsFromProductWithSlug(String slug, int page, int count) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.DESC, "createdAt");
        return this.commentsRepository.findByProductSlug(slug, pageRequest);
    }


    @Override
    public Comment findById(Long id) {
        return getOrThrow(id);
    }

    private Comment getOrThrow(Long id) {
        return findById(id, true);
    }

    private Comment findById(Long id, boolean shouldThrow) {
        if (id == null && shouldThrow)
            throw new ResourceNotFoundException();
        else if (id == null)
            return null;
        Optional<Comment> comment = this.commentsRepository.findById(id);
        if (shouldThrow)
            return comment.orElseThrow(ResourceNotFoundException::new);

        return comment.orElse(null);
    }

    @Override
    public Page<Comment> getCommentsFromUserWithId(Long id, int page, int count) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.DESC, "createdAt");
        return this.commentsRepository.findByAuthor(id, pageRequest);
    }


    @Override
    public Comment findByIdNotThrow(Long id) {
        return findById(id, false);
    }

    @Override
    public void delete(Comment comment) {
        this.commentsRepository.delete(comment);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public Comment update(Comment comment) {
        return this.commentsRepository.save(comment);
    }

    @Override
    public Comment save(String content, Product product, User user) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setUser(user);
        comment.setProduct(product);
        return commentsRepository.save(comment);
    }

    @Override
    public Comment update(Long id, String content, User user) {
        Comment comment = findById(id);
        return update(comment, content, user);
    }

    @Override
    public Comment update(Comment comment, String content, User user) {
        comment.setContent(content);
        comment.setUser(user);
        return this.commentsRepository.save(comment);
    }

    @Override
    public long getCount() {
        return this.commentsRepository.count();
    }

    @Override
    public List<Comment> saveAll(Set<Comment> comments) {
        commentsRepository.saveAll(comments);
        return null;
    }

    @Override
    public List<Object[]> getCommentCountForProductIds(List<Long> productIds) {
        return commentsRepository.findCountForProducts(productIds);
    }
}
