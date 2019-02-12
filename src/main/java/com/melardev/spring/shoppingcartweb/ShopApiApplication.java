package com.melardev.spring.shoppingcartweb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@SpringBootApplication
public class ShopApiApplication {

    public static void main(String[] args) {
        new SpringApplication().setWebApplicationType(WebApplicationType.NONE);
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder()
                .sources(ShopApiApplication.class);

        if (args.length > 0 && args[0].equalsIgnoreCase("seeds")) {
            springApplicationBuilder.profiles("seeds");
        }
        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.run(args);

    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.modules(new JavaTimeModule());

        // for example: Use created_at instead of createdAt
        builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        // skip null fields
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return builder;
    }
}
