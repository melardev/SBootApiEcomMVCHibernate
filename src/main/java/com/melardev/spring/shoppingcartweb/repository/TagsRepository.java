package com.melardev.spring.shoppingcartweb.repository;


import com.melardev.spring.shoppingcartweb.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TagsRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);

    Tag findByNameIgnoreCase(String name);

    @Query("select t from Tag t left join fetch t.images i")
    List<Tag> findAll();

    @Query("select new com.melardev.spring.shoppingcartweb.models.extensions.TagExtension(t.id, t.name, p.id) from Tag t inner join t.products as p where p.id in :ids")
    List<Tag> getTagSummaryFromProducts(Collection<Long> ids);

    @Query("select new com.melardev.spring.shoppingcartweb.models.extensions.TagExtension(t.id, t.name, p.id) from Tag t inner join t.products as p")
    List<Tag> findAllSummary();
}
