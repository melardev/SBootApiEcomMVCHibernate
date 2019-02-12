package com.melardev.spring.shoppingcartweb.services.interfaces;

import com.melardev.spring.shoppingcartweb.models.Category;
import com.melardev.spring.shoppingcartweb.models.Product;

import java.io.File;
import java.util.List;
import java.util.Set;

public interface ICategoryService extends CrudService<Category> {
    Category getByName(String name);

    List<Category> getAll();

    List<Category> getAllSummary();

    void deleteTag(Category category);

    List<Category> findAll();

    Category findOrCreateByName(String name);

    Category findOrCreateByName(String name, String description);

    Category findOrCreate(Category category);

    Set<Category> findOrCreateAll(Set<Category> categories);

    List<Category> saveAll(List<Category> categories);

    List<Category> getAllTags();

    List<Category> getNamesForProducts(List<Product> products);

    List<Category> getNamesForProductIds(List<Long> productIds);

    Category create(String name, String description, List<File> files);
}
