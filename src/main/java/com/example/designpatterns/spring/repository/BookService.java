package com.example.designpatterns.spring.repository;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Domain service. Depends on {@link BookCatalog} only.
 *
 * <p>Because it names no JPA type, it can be unit tested against
 * {@link InMemoryBookCatalog} with no database, no context, and no test containers.
 */
@Service
public class BookService {

    private final BookCatalog catalog;

    public BookService(BookCatalog catalog) {
        this.catalog = catalog;
    }

    public List<Book> byAuthor(String author) {
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("author must not be blank");
        }
        return catalog.findByAuthor(author);
    }

    public Book add(String title, String author, int publishedYear) {
        return catalog.save(new Book(title, author, publishedYear));
    }

    public List<Book> all() {
        return catalog.findAll();
    }
}
