package com.jsw.app.config;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {
    
    @Bean
    public UrlValidator urlValidator() {
        return new UrlValidator();
    }

}