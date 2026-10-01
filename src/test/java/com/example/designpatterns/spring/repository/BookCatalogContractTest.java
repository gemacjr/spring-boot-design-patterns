package com.example.designpatterns.spring.repository;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The shared contract. Both implementations must satisfy it, which is what makes
 * {@link BookCatalog} a real abstraction rather than decoration.
 */
@Transactional
abstract class BookCatalogContractTest {

    // Why @Transactional is here and not only on the @DataJpaTest subclass:
    // Spring resolves the rollback rule from the DECLARING class of each test
    // method, not from the class being run. Without this annotation the inherited
    // methods below execute outside a transaction, rows survive between methods,
    // and the tests pollute each other. It is a no-op for the non-Spring subclass.

    /** Supplied by each subclass: an empty catalog. */
    protected abstract BookCatalog catalog();

    @Test
    void savedBooksAreFoundByAuthor() {
        BookCatalog catalog = catalog();
        catalog.save(new Book("A Wizard of Earthsea", "Ursula K. Le Guin", 1968));
        catalog.save(new Book("Kindred", "Octavia E. Butler", 1979));

        assertThat(catalog.findByAuthor("Ursula K. Le Guin"))
                .extracting(Book::getTitle)
                .containsExactly("A Wizard of Earthsea");
    }

    @Test
    void lookupByAuthorIsCaseInsensitive() {
        BookCatalog catalog = catalog();
        catalog.save(new Book("Kindred", "Octavia E. Butler", 1979));

        assertThat(catalog.findByAuthor("octavia e. butler")).hasSize(1);
        assertThat(catalog.findByAuthor("OCTAVIA E. BUTLER")).hasSize(1);
    }

    @Test
    void anUnknownAuthorYieldsAnEmptyListNotNull() {
        assertThat(catalog().findByAuthor("Nobody At All")).isEmpty();
    }

    @Test
    void savingAssignsAnIdentity() {
        Book saved = catalog().save(new Book("Kindred", "Octavia E. Butler", 1979));

        assertThat(saved.getId()).isNotNull();
    }
}
