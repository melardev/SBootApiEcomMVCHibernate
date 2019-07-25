package com.melardev.spring.shoppingcartweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

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

}
