package com.melardev.spring.shoppingcartweb.models;

import javax.persistence.*;

/**
 * I used Single table inheritance strategy, which makes the table a little bit
 * hard to understand and does not separate concerns across tables, instead everything is put in a single table
 * but this is the second best from the performance point of view(the best is @MappedSuperclass)
 * which is what I would always use, but to showcase a different concept I use this strategy
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "upload_type", discriminatorType = DiscriminatorType.INTEGER)
@Table(name = "file_uploads")
public class FileUpload extends TimestampedEntity {

    protected String filePath;
    protected String fileName;
    protected String originalFileName;
    protected long fileSize;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    Tag tag;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    Category category;

    protected boolean isFeaturedImage;


    public boolean isFeaturedImage() {
        return isFeaturedImage;
    }

    public void setFeaturedImage(boolean featuredImage) {
        isFeaturedImage = featuredImage;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getUrlPath() {
        if (getFilePath().startsWith("http"))
            return getFilePath();
        else
            return "/api" + getFilePath();
    }
}
