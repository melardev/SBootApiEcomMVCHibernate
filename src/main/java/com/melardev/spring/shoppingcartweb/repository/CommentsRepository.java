package com.melardev.spring.shoppingcartweb.repository;


import com.melardev.spring.shoppingcartweb.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE c.product.slug = :slug")
    Page<Comment> findByProductSlug(@Param("slug") String slug, Pageable pageRequest);

    @Query("SELECT c FROM Comment c WHERE c.user.id = :id")
    Page<Comment> findByAuthor(@Param("id") Long id, Pageable pageRequest);

    @Query("select c.product.id, count(*) from Comment c where c.product.id in :productIds group by c.product.id")
    List<Object[]> findCountForProducts(@Param("productIds") List<Long> productIds);
}
