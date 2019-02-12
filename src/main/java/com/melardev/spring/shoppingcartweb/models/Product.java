package com.melardev.spring.shoppingcartweb.models;


import com.melardev.spring.shoppingcartweb.enums.ContentType;
import com.melardev.spring.shoppingcartweb.services.StringHelper;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Table(name = "products")
@Entity
public class Product extends TimestampedEntity {

    @Column(nullable = false)
    public String name;

    @Type(type = "text")
    protected String description;

    public String slug;

    protected double price;
    private static final SimpleDateFormat SLUG_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    // One product has many comments, OneToMany has to have the mappedBy, or problems otherwiese
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
    private Collection<Comment> comments = new ArrayList<>();

    // One product has many tags, One tag belongs to many products. The owning side defines the Join Columns and tables
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "products_tags",
            joinColumns = { // Owning side first(joinColumns:product), then the other side(inverseJoin: tag)
                    @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", nullable = false, referencedColumnName = "id")})
    protected Set<Tag> tags = new HashSet<>();


    @LazyCollection(LazyCollectionOption.FALSE)
    // One product has many tags, One tag belongs to many products. The owning side defines the Join Columns and tables
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "products_categories",
            joinColumns = {
                    @JoinColumn(name = "product_id", nullable = false, referencedColumnName = "id")},
            inverseJoinColumns = {
                    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "id")})
    protected Set<Category> categories = new HashSet<>();

    // One product belongs to many orderItems, One order item points to one single product, OneToMany has to have the mappedBy, or problems otherwiese
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.REMOVE)
    private Collection<Comment> orderItems = new ArrayList<>();

    private Integer views = 0;
    @Column(nullable = false)
    protected ZonedDateTime publishOn;

    @Enumerated(EnumType.STRING)
    private ContentType contentType;
    protected int stock;


    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    protected
    Collection<ProductImage> images;

    @Transient
    private Long commentCount;


    public Product(String name, String description, String slug, double price, ZonedDateTime createdAt) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.setCreatedAt(createdAt);
    }

    public Product() {
    }

    public Product(String name, String description, String slug, double price, Set<Category> categories, Set<Tag> tags) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.price = price;
        this.categories = categories;
        this.tags = tags;
    }

    public Integer getViews() {
        return views == null ? 0 : views;
    }

    public void setViews(Integer views) {
        this.views = views;
    }

    public void incrementViews() {
        if (views == null)
            views = 1;
        else
            views++;
    }

    public String getRenderedDescription() {
        String rendered = getDescription();
        if (this.contentType == ContentType.MARKDOWN) {
            // Not very readable, but, we are only parsing the content using a Markdow parser
            rendered = StringHelper.markdownToHtml(rendered);
        }
        return rendered;
    }

    public double getPrice() {
        DecimalFormat dcf = new DecimalFormat("#.##");
        return Double.valueOf(dcf.format(price));
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public String getSlug() {
        return slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }


    //
    //  The below are not needed since the product service take care of slugifying the products
    //  before update or update, but just for completness
    //

    @PrePersist
    public void preCreate() {
        slugifyIfEmptySlug();
        if (publishOn == null || publishOn.isBefore(ZonedDateTime.now()))
            publishOn = ZonedDateTime.now();
    }

    @PreUpdate
    public void preUpdateEntity() {
        slugifyIfEmptySlug();
    }

    private void slugifyIfEmptySlug() {
        if (StringHelper.isEmpty(getSlug()))
            setSlug(StringHelper.slugify(getName()));

        if (publishOn == null || publishOn.isBefore(ZonedDateTime.now()))
            publishOn = ZonedDateTime.now();
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public Collection<Comment> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Collection<Comment> orderItems) {
        this.orderItems = orderItems;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ZonedDateTime getPublishOn() {
        return publishOn;
    }

    public void setPublishOn(ZonedDateTime publishOn) {
        this.publishOn = publishOn;
    }

    public Collection<ProductImage> getImages() {
        if (images == null)
            images = new ArrayList<>();
        return images;
    }

    public void setImages(Collection<ProductImage> images) {
        this.images = images;
    }


    public void setCommentsCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }
}
