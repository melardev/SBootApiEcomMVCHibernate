package com.melardev.spring.shoppingcartweb.repository;


import com.melardev.spring.shoppingcartweb.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductsRepository extends JpaRepository<Product, Long> {

    void delete(Product deleted);

    List<Product> findAll();

    Page<Product> findAll(Pageable pageRequest);

    Optional<Product> findById(Long id);

    @Query("select p from Product p left join fetch p.tags t left join fetch p.categories c left join fetch p.comments c " +
            "where p.slug = :slug")
    Optional<Product> findBySlug(@Param("slug") String slug);


    @Query(value = "SELECT * FROM products order by rand() limit 1", nativeQuery = true)
    Product findRandom();

    @Query("select new com.melardev.spring.shoppingcartweb.models.extensions.ProductExtension(p.id, p.name, p.slug, p.price, p.stock, p.publishOn) from Product p")
    Page<Product> findAllForSummary(PageRequest pageRequest);

    @Query("from Product p where p.id not in :productIds")
    List<Product> findRandomNotIn(@Param("productIds") List<Long> ids);

    @Query("select new com.melardev.spring.shoppingcartweb.models.extensions.ProductExtension(p.id, p.price) from Product p where p.id=:id")
    Product findByIdForPrice(@Param("id") Long id);

    @Query("select new com.melardev.spring.shoppingcartweb.models.extensions.ProductExtension(p.id,p.name, p.slug, p.price) from Product p where p.id=:id")
    Product findByIdWithElementalInfo(@Param("id") Long id);

    @Query("select new com.melardev.spring.shoppingcartweb.models.extensions.ProductExtension(p.id, p.name, p.slug, p.price) from Product p where p.id in :ids")
    List<Product> findBasicInfoWhereProductIds(@Param("ids") List<Long> productIds);

    void flush();

    Product save(Product product);

    @Query("SELECT p FROM Product p INNER JOIN p.categories c WHERE c.name = :category")
    Page<Product> findByCategory(@Param("category") String category, Pageable pageable);

    @Query("SELECT p FROM Product p INNER JOIN p.tags t WHERE t.name = :tag")
    Page<Product> findByTagName(@Param("tag") String tag, Pageable pageable);

    @Query("select new com.melardev.spring.shoppingcartweb.models.extensions.ProductExtension(p.id, p.name, p.slug, p.price,p.stock, p.publishOn) from Product p INNER JOIN p.tags t WHERE t.name= :tag")
    Page<Product> findBasicInfoByTagName(@Param("tag") String tag, Pageable pageable);

    Page<Product> findByDescriptionContainsOrNameContainsAllIgnoreCase(String description, String name, Pageable pageReguest);

    //List<Product> findByDescriptionContainsOrNameContainsAllIgnoreCase(String content, String name, Pageable pageRequest);
    //Slice<Product> findByDescriptionContainsOrNameContainsAllIgnoreCase(String content, String name, Pageable pageRequest);

    Long countByNameContains(String firstName);

    // Only for reference, do not use it, Modifying is needed for Spring Data < 1.7
    @Modifying
    @Transactional
    List<Product> removeByName(String firstName);

}