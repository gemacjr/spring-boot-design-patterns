package com.example.designpatterns.spring.repository;

/**
 * The API shape for a {@link Book}.
 *
 * <p>{@code Book} is a JPA entity: mutable, lazily-loaded in places, and owned by
 * the persistence layer. Serialising it straight out of a controller is the exact
 * mistake the DTO package warns about, so this package does not do it either — the
 * lesson has to hold everywhere or it is not a lesson.
 *
 * <p>It lives here rather than reusing the DTO package's types because no pattern
 * package depends on another; see {@code docs/patterns/README.md}.
 */
public record BookView(Long id, String title, String author, int publishedYear) {

    public static BookView of(Book book) {
        return new BookView(book.getId(), book.getTitle(), book.getAuthor(), book.getPublishedYear());
    }
}
