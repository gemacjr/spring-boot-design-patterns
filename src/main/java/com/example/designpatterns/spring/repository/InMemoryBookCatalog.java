package com.example.designpatterns.spring.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The hand-written repository Spring Data saves you from writing.
 *
 * <p>It exists to make one point: it satisfies exactly the same
 * {@link BookCatalog} contract and passes exactly the same tests. The pattern is
 * the contract; Spring Data is one implementation strategy for it.
 *
 * <p>It is also genuinely useful — a fast, dependency-free double for unit tests
 * that would otherwise need a database.
 *
 * <p>Full documentation: {@code docs/patterns/repository.md}
 */
public class InMemoryBookCatalog implements BookCatalog {

    private final List<Book> books = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Book save(Book book) {
        assignId(book, sequence.incrementAndGet());
        books.add(book);
        return book;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return books.stream()
                .filter(book -> book.getAuthor().equalsIgnoreCase(author))
                .toList();
    }

    @Override
    public List<Book> findAll() {
        return List.copyOf(books);
    }

    @Override
    public long count() {
        return books.size();
    }

    /**
     * JPA assigns ids through reflection too; doing the same here keeps {@link Book}
     * free of a setter that application code should never call.
     */
    private static void assignId(Book book, long id) {
        try {
            Field field = Book.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(book, id);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("could not assign id", e);
        }
    }
}
