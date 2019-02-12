package com.melardev.spring.shoppingcartweb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/uploads/")) {
            registry.addResourceHandler("/uploads").addResourceLocations(System.getProperty("user.dir") + "/uploads");
        }
    }
}
