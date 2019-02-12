package com.melardev.spring.shoppingcartweb.repository;

import com.melardev.spring.shoppingcartweb.models.FileUpload;
import com.melardev.spring.shoppingcartweb.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileUploadRepository extends JpaRepository<FileUpload, Long> {

    @Query("select new com.melardev.spring.shoppingcartweb.models.extensions.ProductImageExtension(f.product.id, f.filePath) from FileUpload f where f.product.id in :productIds")
    List<ProductImage> findAllWhereProductIdIn(List<Long> productIds);
}
