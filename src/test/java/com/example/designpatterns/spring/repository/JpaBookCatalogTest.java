package com.example.designpatterns.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The identical contract, run against H2 through Spring Data.
 *
 * <p>{@code @DataJpaTest} is a slice test: it starts only the persistence layer,
 * not the web layer, and rolls back each test. {@code data.sql} seeding is disabled
 * here so the contract tests start from an empty schema.
 */
@DataJpaTest
@Import(JpaBookCatalog.class)
@TestPropertySource(properties = "spring.sql.init.mode=never")
class JpaBookCatalogTest extends BookCatalogContractTest {

    @Autowired JpaBookCatalog jpaCatalog;
    @Autowired BookRepository repository;

    @Override
    protected BookCatalog catalog() {
        return jpaCatalog;
    }

    @Test
    void springDataDerivedTheQueryFromTheMethodNameAlone() {
        repository.save(new Book("The Dispossessed", "Ursula K. Le Guin", 1974));

        assertThat(repository.findByAuthorIgnoreCase("ursula k. le guin")).hasSize(1);
        assertThat(BookRepository.class.getDeclaredMethods()).hasSize(1);
        assertThat(BookRepository.class.isInterface()).isTrue();
    }
}
