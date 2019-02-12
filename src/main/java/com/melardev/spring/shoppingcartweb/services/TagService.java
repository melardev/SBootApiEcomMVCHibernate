package com.melardev.spring.shoppingcartweb.services;

import com.melardev.spring.shoppingcartweb.models.Product;
import com.melardev.spring.shoppingcartweb.models.Tag;
import com.melardev.spring.shoppingcartweb.models.TimestampedEntity;
import com.melardev.spring.shoppingcartweb.repository.TagsRepository;
import com.melardev.spring.shoppingcartweb.services.interfaces.ITagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.File;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class TagService implements ITagService {
    private TagsRepository tagRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    public TagService(TagsRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag findByName(String tagName) {
        return tagRepository.findByName(tagName).orElse(null);
    }

    @Override
    public void deleteTag(Tag tag) {
        tagRepository.delete(tag);
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> findAllForSummary() {
        return tagRepository.findAllSummary();
    }

    @Override
    public Tag findOrCreateByName(String name) {
        return findOrCreateByName(name, null);
    }

    @Override
    public Tag findOrCreateByName(String name, String description) {
        Tag tag = tagRepository.findByName(name).orElse(null);
        if (tag == null) {
            tag = tagRepository.save(new Tag(name, description));
        }
        return tag;
    }

    @Override
    public Tag findOrCreate(Tag tag) {
        Tag t = tagRepository.findByNameIgnoreCase(tag.getName());
        if (t == null)
            t = tagRepository.save(new Tag(tag.getName(), tag.getDescription()));

        return t;
    }

    @Override
    public Set<Tag> findOrCreateAll(Set<Tag> tags) {
        if (tags == null)
            return null;
        tags = tags.stream().map(t -> t = findOrCreate(t)).collect(Collectors.toSet());
        return tags;
    }

    @Override
    public List<Tag> findAll() {
        List<Tag> result = this.tagRepository.findAll();
        return result;
    }

    @Override
    public Page<Tag> findLatest(int page, int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.Direction.DESC, "createdAt");
        Page<Tag> result = this.tagRepository.findAll(pageRequest);
        return result;
    }

    @Override
    public List<Tag> findTagNamesForProducts(List<Product> products) {
        List<Long> ids = products.stream().map(TimestampedEntity::getId).collect(Collectors.toList());
        return findTagNamesForProductIds(ids);
    }

    @Override
    public List<Tag> findTagNamesForProductIds(List<Long> productIds) {
        return tagRepository.getTagSummaryFromProducts(productIds);
    }


    @Override
    public List<Tag> saveAll(List<Tag> tags) {
        return tagRepository.saveAll(tags);
    }

    @Override
    public long getAllCount() {
        return tagRepository.count();
    }

    @Override
    public Tag getRandom() {
        Query countQuery = em.createNativeQuery("select count(*) from Tag");
        long count = (Long) countQuery.getSingleResult();

        Random random = new Random();
        int number = random.nextInt((int) count);

        Query selectQuery = em.createQuery("select t from Tag t");
        selectQuery.setFirstResult(number);
        selectQuery.setMaxResults(1);
        return (Tag) selectQuery.getSingleResult();
    }

    @Override
    public List<Tag> saveAll(Set<Tag> tags) {
        return tagRepository.saveAll(tags);
    }

    @Override
    public Tag update(Tag tag) {
        return tagRepository.save(tag);
    }


    @Override
    public Tag create(String name, String description, List<File> files) {
        Tag tag = new Tag();
        tag.setName(name);
        tag.setDescription(description);
        tag.setImages(fileUploadService.saveImages(tag, files));

        return tagRepository.save(tag);
    }

    @Override
    public void delete(Tag tag) {
        tagRepository.delete(tag);
    }

    @Override
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

}
