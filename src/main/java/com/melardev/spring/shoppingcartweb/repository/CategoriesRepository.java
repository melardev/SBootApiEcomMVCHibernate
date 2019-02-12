package com.melardev.spring.shoppingcartweb.repository;

import com.melardev.spring.shoppingcartweb.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    @Query("select c from Category c left join fetch c.images i")
    List<Category> findAllSummary();

    Category findByNameIgnoreCase(String name);

    @Query(value = "SELECT * FROM categories order by rand() limit 1", nativeQuery = true)
    Category getRandom();

    @Query("select new com.melardev.spring.shoppingcartweb.models.extensions.CategoryExtension(c.id, c.name, p.id) from Category c inner join c.products as p where p.id in :ids")
    List<Category> getCategorySummaryFromProducts(@Param("ids") List<Long> productIds);
}
