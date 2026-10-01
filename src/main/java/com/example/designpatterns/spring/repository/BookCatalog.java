package com.example.designpatterns.spring.repository;

import java.util.List;

/**
 * <b>Repository</b>: a collection-like interface over persistent objects.
 *
 * <p>The pattern is the <em>interface</em>, not the technology. Domain code asks
 * for books by author; whether that becomes SQL, a REST call, or a
 * {@code HashMap} lookup is invisible from here. Two implementations in this
 * package satisfy this same contract, and the same test contract passes against
 * both — which is the proof that the abstraction is real and not decorative.
 *
 * <p>Full documentation: {@code docs/patterns/repository.md}
 */
public interface BookCatalog {

    Book save(Book book);

    List<Book> findByAuthor(String author);

    List<Book> findAll();

    long count();
}
