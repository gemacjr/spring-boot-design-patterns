package com.example.designpatterns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the design patterns showcase.
 *
 * <p>This class deliberately lives in the root package. {@code @SpringBootApplication}
 * implies {@code @ComponentScan} rooted at the annotated class's package, so every
 * pattern package beneath {@code com.example.designpatterns} is discovered without
 * any explicit registration.
 *
 * <p>See {@code docs/patterns/README.md} for the catalog of patterns demonstrated here.
 */
@SpringBootApplication
public class DesignPatternsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesignPatternsApplication.class, args);
    }
}
