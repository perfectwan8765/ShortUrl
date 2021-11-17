package com.jsw.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import javafx.application.Application;

@SpringBootApplication
public class ShortUrlApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ShortUrlApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

}
