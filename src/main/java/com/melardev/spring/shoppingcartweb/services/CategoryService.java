package com.melardev.spring.shoppingcartweb.services;

import com.melardev.spring.shoppingcartweb.models.Category;
import com.melardev.spring.shoppingcartweb.models.Product;
import com.melardev.spring.shoppingcartweb.models.TimestampedEntity;
import com.melardev.spring.shoppingcartweb.repository.CategoriesRepository;
import com.melardev.spring.shoppingcartweb.services.interfaces.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryService implements ICategoryService {
    private CategoriesRepository categoriesRepository;
    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    public CategoryService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    @Override
    public Category getByName(String name) {
        return categoriesRepository.findByName(name).orElse(null);
    }

    @Override
    public List<Category> getAll() {
        return categoriesRepository.findAll();
    }

    @Override
    public List<Category> getAllSummary() {
        return categoriesRepository.findAllSummary();
    }

    @Override
    public void deleteTag(Category category) {
        categoriesRepository.delete(category);
    }

    @Override
    public List<Category> findAll() {
        return categoriesRepository.findAll();
    }


    @Override
    public Category findOrCreateByName(String name) {
        return findOrCreateByName(name, null);
    }

    @Override
    public Category findOrCreateByName(String name, String description) {
        Category category = categoriesRepository.findByName(name).orElse(null);
        if (category == null) {
            category = categoriesRepository.save(new Category(name, description));
        }
        return category;
    }

    @Override
    public Category findOrCreate(Category category) {
        Category c = categoriesRepository.findByNameIgnoreCase(category.getName());
        if (c == null)
            c = categoriesRepository.save(new Category(category.getName(), category.getDescription()));

        return c;
    }

    @Override
    public Set<Category> findOrCreateAll(Set<Category> categories) {
        if (categories == null)
            return null;
        categories = categories.stream().map(category -> category = findOrCreate(category)).collect(Collectors.toSet());
        return categories;
    }

    @Override
    public Page<Category> findLatest(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "createdAt");
        Page<Category> result = this.categoriesRepository.findAll(pageRequest);
        return result;
    }

    @Override
    public long getAllCount() {
        return categoriesRepository.count();
    }

    @Override
    public Category getRandom() {
        return categoriesRepository.getRandom();
    }

    @Override
    public List<Category> saveAll(Set<Category> products) {
        categoriesRepository.saveAll(products);
        return null;
    }

    @Override
    public Category update(Category category) {
        return categoriesRepository.save(category);
    }

    @Override
    public void delete(Category category) {
        categoriesRepository.delete(category);
    }

    @Override
    public void delete(Long id) {
        categoriesRepository.deleteById(id);
    }

    @Override
    public List<Category> saveAll(List<Category> categories) {
        return categoriesRepository.saveAll(categories);
    }

    @Override
    public List<Category> getAllTags() {
        return categoriesRepository.findAll();
    }

    @Override
    public List<Category> getNamesForProducts(List<Product> products) {
        List<Long> ids = products.stream().map(TimestampedEntity::getId).collect(Collectors.toList());
        return getNamesForProductIds(ids);
    }

    @Override
    public List<Category> getNamesForProductIds(List<Long> productIds) {
        return categoriesRepository.getCategorySummaryFromProducts(productIds);
    }

    @Override
    public Category create(String name, String description, List<File> files) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setImages(fileUploadService.saveImages(category, files));

        return categoriesRepository.save(category);
    }
}
