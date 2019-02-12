package com.melardev.spring.shoppingcartweb.services;

import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Service
public class ReflectionMapperService {

    static class Mappings {
        Class source;
        Class destination;
        Map<String, Method> gettersSource;
        Map<String, Method> settersSource;

        Map<String, Method> gettersDestination;
        Map<String, Method> settersDestination;


        public Mappings() {
            gettersSource = new HashMap<>();
            settersSource = new HashMap<>();
            gettersDestination = new HashMap<>();
            settersDestination = new HashMap<>();
        }
    }

    List<Mappings> mappings;

    public ReflectionMapperService() {
        mappings = new ArrayList<>();
    }

    public <S, T> T map(S source, Class<T> targetClass) throws IllegalAccessException, InstantiationException {
        Mappings mapping = mappings.stream().peek(new Consumer<Mappings>() {
            @Override
            public void accept(Mappings mappings) {

            }
        }).filter(new Predicate<Mappings>() {
            @Override
            public boolean test(Mappings mapping) {
                return mapping.source == source.getClass() && mapping.destination == targetClass;
            }
        }).findFirst().orElse(null);

        if (mapping == null) {
            mapping = new Mappings();
            Method[] methods = source.getClass().getMethods();
            for (Method method : methods) {
                if (isGetter(method))
                    mapping.gettersSource.put(method.getName(), method);

                if (isSetter(method))
                    mapping.settersSource.put(method.getName(), method);

            }

            methods = targetClass.getMethods();
            for (Method method : methods) {
                if (isGetter(method))
                    mapping.gettersDestination.put(method.getName(), method);

                if (isSetter(method))
                    mapping.settersDestination.put(method.getName(), method);

            }

        }
        return targetClass.newInstance();
    }

    private boolean isGetter(Method method) {
        if (!method.getName().startsWith("findById")) return false;
        if (method.getParameterTypes().length != 0) return false;
        if (void.class.equals(method.getReturnType())) return false;
        return true;
    }

    public static boolean isSetter(Method method) {
        if (!method.getName().startsWith("set")) return false;
        if (method.getParameterTypes().length != 1) return false;
        return true;
    }
}
