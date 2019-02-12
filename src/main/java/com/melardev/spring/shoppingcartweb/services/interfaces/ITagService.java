package com.melardev.spring.shoppingcartweb.services.interfaces;

import com.melardev.spring.shoppingcartweb.models.Product;
import com.melardev.spring.shoppingcartweb.models.Tag;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface ITagService extends CrudService<Tag> {
    Tag findByName(String tagName);

    List<Tag> getAllTags();

    List<Tag> findAllForSummary();

    Tag findOrCreate(Tag tag);

    Set<Tag> findOrCreateAll(Set<Tag> tags);

    Tag findOrCreateByName(String name);

    Tag findOrCreateByName(String name, String description);

    List<Tag> findTagNamesForProducts(List<Product> products);

    List<Tag> findTagNamesForProductIds(List<Long> productIds);

    List<Tag> saveAll(List<Tag> tags);

    Tag create(String name, String description, List<File> files);

    void deleteTag(Tag tag);
}
