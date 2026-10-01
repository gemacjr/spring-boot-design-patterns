package com.example.designpatterns.spring.repository;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

/** Satisfies {@link BookCatalog} by delegating to Spring Data. */
@Repository
@Primary
public class JpaBookCatalog implements BookCatalog {

    private final BookRepository repository;

    public JpaBookCatalog(BookRepository repository) {
        this.repository = repository;
    }

    @Override public Book save(Book book) { return repository.save(book); }

    @Override public List<Book> findByAuthor(String author) {
        return repository.findByAuthorIgnoreCase(author);
    }

    @Override public List<Book> findAll() { return repository.findAll(); }

    @Override public long count() { return repository.count(); }
}
