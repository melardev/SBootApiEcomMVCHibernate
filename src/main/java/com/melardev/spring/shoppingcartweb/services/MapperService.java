package com.melardev.spring.shoppingcartweb.services;

import org.springframework.stereotype.Service;

@Service
public class MapperService {
/*
    private ModelMapper mapper;

    public MapperService() {
        this.mapper = new ModelMapper();
        this.mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull())
                .setFieldMatchingEnabled(true)
                .setMethodAccessLevel(Configuration.AccessLevel.PUBLIC)
                .setSourceNamingConvention(NamingConventions.NONE)
                .setDestinationNamingConvention(NamingConventions.NONE);

  /*      this.mapper.createTypeMap(Product.class, ProductSummaryDto.class)
                .addMapping(Product::getName, ProductSummaryDto::setName)
                .addMapping(Product::getContent, ProductSummaryDto::setContent);
*/
     /*   this.mapper.createTypeMap(Product.class, ProductSummaryDto.class)
                .addMapping(Product::getName, ProductSummaryDto::setName)
                .addMapping(Product::getSlug, ProductSummaryDto::setSlug)
                .addMapping(Product::getDescription, ProductSummaryDto::setDescription);

        /*
        this.mapper.createTypeMap(Order.class, OrderSummaryDto.class)
                .addMapping(Order::getUser, OrderSummaryDto::setUser);
        */
/*
        this.mapper.createTypeMap(Comment.class, CreateCommentDto.class)
                .addMapping(Comment::getId, CreateCommentDto::setId); // I don't know why but without this, the Id is not mapped

        // Inheritance
        this.mapper.createTypeMap(Product.class, AdminProductSummaryDto.class)
                .includeBase(Product.class, ProductSummaryDto.class);
    }

    public <S, T> T map(S source, Class<T> targetClass) {
        return getMapper().map(source, targetClass);
    }

    private ModelMapper getMapper() {
        return mapper;
    }

    public <S, T> void mapTo(S source, T dist) {
        getMapper().map(source, dist);
    }

    public <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        List<T> list = new ArrayList<>();
        for (S s : source) {
            list.add(getMapper().map(s, targetClass));
        }
        return list;
    }

    public <T, S> Collection<T> mapStreamToCollectionSetOf(Streamable<S> collection, Class<T> target) {
        Set<T> result = collection
                .stream().map(item -> map(item, target))
                .collect(Collectors.toSet());
        return result;
    }

    public <T, S> Collection<T> mapStreamToCollectionSetOf(Collection<S> collection, Class<T> target) {
        Set<T> result = collection
                .stream().map(item -> map(item, target))
                .collect(Collectors.toSet());
        return result;
    }
    */
}