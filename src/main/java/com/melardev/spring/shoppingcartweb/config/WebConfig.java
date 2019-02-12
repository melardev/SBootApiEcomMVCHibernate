package com.melardev.spring.shoppingcartweb.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;


@Configuration
public class WebConfig {

    @Bean
    public MessageSource messageSource() {
        // setBasenames(.., ..) if I will, see LocaleInterceptor by Baeldung
        ReloadableResourceBundleMessageSource bundle = new ReloadableResourceBundleMessageSource();
        bundle.setBasename("classpath:messages");
        bundle.setBasename("classpath:errors/errors");
        bundle.setDefaultEncoding("UTF-8");
        return bundle;
    }

    @Bean
    public LocalValidatorFactoryBean validator() {
        LocalValidatorFactoryBean lvfb = new LocalValidatorFactoryBean();
        lvfb.setValidationMessageSource(messageSource());
        return lvfb;
    }

}