package com.melardev.spring.shoppingcartweb.services;


import com.melardev.spring.shoppingcartweb.errors.exceptions.ResourceNotFoundException;
import com.melardev.spring.shoppingcartweb.models.Category;
import com.melardev.spring.shoppingcartweb.models.Product;
import com.melardev.spring.shoppingcartweb.models.ProductImage;
import com.melardev.spring.shoppingcartweb.models.Tag;
import com.melardev.spring.shoppingcartweb.models.extensions.CategoryExtension;
import com.melardev.spring.shoppingcartweb.models.extensions.ProductImageExtension;
import com.melardev.spring.shoppingcartweb.models.extensions.TagExtension;
import com.melardev.spring.shoppingcartweb.repository.ProductsRepository;
import com.melardev.spring.shoppingcartweb.services.interfaces.IProductsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductsService implements IProductsService {

    private ProductsRepository productsRepository;


    @Autowired
    StorageService storageService;
    @Autowired
    FileUploadService fileUploadService;
    @Autowired
    private TagService tagService;

    @Autowired
    private CategoryService categoriesService;

    @Autowired
    private CommentsService commentsService;

    @Autowired
    public ProductsService(ProductsRepository productsRepository) {
        this.productsRepository = productsRepository;
    }

    @Override
    public Product findById(Long id) throws ResourceNotFoundException {
        return this.productsRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public Page<Product> findLatest(int page, int count) {
        PageRequest pageRequest = PageRequest.of(page, count, Sort.Direction.DESC, "createdAt");
        return this.productsRepository.findAll(pageRequest);
    }

    @Override
    public Page<Product> findAllForSummary(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "createdAt");
        Page<Product> result = this.productsRepository.findAllForSummary(pageRequest);
        // Why not just calling this.productsRepository.findAll(pageRequest) ? because the below code is faster
        // but you could call findAll() and then you will also need to make a request to comments table to fetch comments count
        List<Product> products = result.getContent();
        List<Long> productIds = products.stream().map(p -> p.getId()).collect(Collectors.toList());

        List<Tag> tags = tagService.findTagNamesForProductIds(productIds);
        List<Category> categories = categoriesService.getNamesForProductIds(productIds);
        List<Object[]> commentCounts = commentsService.getCommentCountForProductIds(productIds);
        List<ProductImage> productImages = fileUploadService.getProductImagesFrom(productIds);

        // this is done to iterate once, instead of iterating all(MANY) the products for each(MANY), then for each category and then comment
        // I iterate in (ONE) pass over categories, tags, and commentsCount, then the products are iterated in nested loop (MANY)
        // that approach is implemented in OrderService::findOrderSummariesBelongingToUser orderItems.forEach blabla
        int lastIndex = Math.max(commentCounts.size(), Math.max(tags.size(), Math.max(productImages.size(), categories.size())));

        TagExtension tag = null;
        CategoryExtension category = null;
        int index = -1;
        ProductImageExtension productImage = null;
        Product product;
        for (int i = 0; i < lastIndex; i++) {
            if (i < tags.size())
                tag = (TagExtension) tags.get(i);
            if (i < categories.size())
                category = (CategoryExtension) categories.get(i);

            if (commentCounts.size() - 1 > i)
                index = i;
            else
                index = -1;


            if (productImages.size() - 1 > i)
                productImage = (ProductImageExtension) productImages.get(i);

            for (int j = 0; j < products.size(); j++) {
                product = products.get(j);

                if (tag != null && product.getId().equals(tag.getProductId()))
                    product.getTags().add(tag);

                if (category != null && product.getId().equals(category.getProductId()))
                    product.getCategories().add(category);

                if (index != -1 && product.getId().equals(commentCounts.get(index)[0]))
                    product.setCommentsCount((Long) commentCounts.get(index)[1]);

                if (productImage != null && productImage.getProductId().equals(product.getId()))
                    product.getImages().add(productImage);
            }
        }

        return result;

    }


    @Override
    public long getAllCount() {
        return productsRepository.count();
    }

    @Override
    public Page<Product> findByTagName(String tagName, int page, int count) {
        PageRequest pageRequest = PageRequest.of(page - 1, count, Sort.Direction.DESC, "createdAt");
        //Page<Product> products = productsRepository.findByTagName(tagName, pageRequest);
        Page<Product> products = productsRepository.findBasicInfoByTagName(tagName, pageRequest);
        return products;
    }

    @Override
    public Product create(Product product) {
        if (StringHelper.isEmpty(product.getSlug()))
            product.setSlug(StringHelper.slugify(product.getName()));
        return this.productsRepository.save(product);
    }

    @Override
    public Product create(Product product, String tags, String categories) {
        product.setTags(parseTags(tags));
        product.setCategories(parseCategories(categories));
        return create(product);
    }

    @Override
    public Product update(Product product) {
        if (StringHelper.isEmpty(product.getSlug()))
            product.setSlug(StringHelper.slugify(product.getName()));

        return this.productsRepository.save(product);
    }

    @Override
    public void delete(Long id) {
        this.productsRepository.deleteById(id);
    }

    @Override
    public void delete(Product product) {
        this.productsRepository.delete(product);
    }

    private Set<Category> parseCategories(String categoriesCSV) {
        Set<Category> categories = new HashSet<>();
        if (!StringHelper.isEmpty(categoriesCSV)) {
            categories = Arrays.stream(categoriesCSV.split(","))
                    .map(name -> new Category(name.trim())).collect(Collectors.toSet());
        }

        return categories;
    }

    private Set<Tag> parseTags(String tagsCSV) {
        Set<Tag> tags = new HashSet<>();
        if (!StringHelper.isEmpty(tagsCSV)) {
            tags = Arrays.stream(tagsCSV.split(","))
                    .map(name -> new Tag(name.trim())).collect(Collectors.toSet());
        }
        return tags;
    }


    @Override
    public Product findBySlug(String slug) {
        return findBySlug(slug, true);
    }


    private Product findBySlug(String slug, boolean shouldThrow) {
        Optional<Product> product = this.productsRepository.findBySlug(slug);
        if (shouldThrow)
            return product.orElseThrow(ResourceNotFoundException::new);

        return product.orElse(null);
    }

    @Override
    public List<Product> findAll() {
        return this.productsRepository.findAll();
    }

    @Override
    public Product findById(String slug) {
        return productsRepository.findBySlug(slug).orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<Product> saveAll(Set<Product> products) {
        return this.productsRepository.saveAll(products);
    }


    @Override
    public Product save(Product product) {
        return productsRepository.save(product);
    }

    @Override
    public Product getRandom() {
        // to see other implementation using EntityManager look TagService.getRandom
        return productsRepository.findRandom();
    }

    public List<Product> findRandomNotIn(List<Long> ids) {
        return productsRepository.findRandomNotIn(ids);
    }


    public Product getReference(Long productId) {
        return this.productsRepository.getOne(productId);
    }

    public Product getProductForPrice(Long id) {
        return productsRepository.findByIdForPrice(id);
    }

    public Product findByIdWithElementalInfo(Long id) {
        return productsRepository.findByIdWithElementalInfo(id);
    }

    public Product create(String name, String description, Set<Tag> tags, Set<Category> categories, List<File> files) throws IOException {
        Product product = new Product();
        tags = tagService.findOrCreateAll(tags);
        categories = categoriesService.findOrCreateAll(categories);
        product.setName(name);
        product.setDescription(description);
        product.setTags(tags);
        product.setCategories(categories);
        // product = productsRepository.save(product);
        product.setImages(fileUploadService.saveImages(product, files));

        return productsRepository.save(product);
    }


    public Product createWithDetachedTagsAndCategories(Product product, List<File> files) {
        Set<Tag> tags = tagService.findOrCreateAll(product.getTags());
        Set<Category> categories = categoriesService.findOrCreateAll(product.getCategories());

        product.setTags(tags);
        product.setCategories(categories);
        product.setImages(fileUploadService.saveImages(product, files));

        return productsRepository.save(product);
    }

    @Override
    public List<Product> findBasicInfoWhereProductIds(List<Long> productIds) {
        return productsRepository.findBasicInfoWhereProductIds(productIds);
    }

    @Override
    public Page<Product> getByCategory(String categoryName, int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "createdAt");
        return productsRepository.findByCategory(categoryName, pageRequest);
    }
}

