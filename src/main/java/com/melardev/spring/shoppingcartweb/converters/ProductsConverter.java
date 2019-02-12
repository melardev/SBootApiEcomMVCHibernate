package com.melardev.spring.shoppingcartweb.converters;

import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.MultiValueMap;

import java.io.IOException;


// TODO: Implement it so I can retrieve cart items from the form and return a Set<CartItem>
public class ProductsConverter extends FormHttpMessageConverter {


    public MultiValueMap<String, String> read(Class<? extends MultiValueMap<String, ?>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }
}
