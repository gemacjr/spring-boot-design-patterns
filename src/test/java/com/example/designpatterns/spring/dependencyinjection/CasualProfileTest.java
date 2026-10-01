package com.example.designpatterns.spring.dependencyinjection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The decisive test: activating a profile swaps the implementation with no change
 * to {@code GreetingFacade} or to either greeting service.
 */
@SpringBootTest
@ActiveProfiles("casual")
class CasualProfileTest {

    @Autowired GreetingFacade facade;

    @Test
    void theCasualProfileSwapsTheImplementation() {
        assertThat(facade.implementationName()).isEqualTo("CasualGreetingService");
        assertThat(facade.greet("Ada")).startsWith("Hey Ada!");
    }
}
