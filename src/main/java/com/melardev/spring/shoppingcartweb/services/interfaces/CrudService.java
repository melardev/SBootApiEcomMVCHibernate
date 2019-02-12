package com.melardev.spring.shoppingcartweb.services.interfaces;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface CrudService<T> {

    List<T> findAll();
    Page findLatest(int pageSize, int skip);
    long getAllCount();
    T getRandom();

    List<T> saveAll(Set<T> products);
    T update(T object);

    void delete(T t);
    void delete(Long id);
}
