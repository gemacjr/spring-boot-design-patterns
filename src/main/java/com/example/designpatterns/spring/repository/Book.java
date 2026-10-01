package com.example.designpatterns.spring.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/**
 * The aggregate this repository manages.
 *
 * <p>A class rather than a record: JPA requires a no-arg constructor and mutable
 * fields for its proxying and dirty-checking, so entities cannot be records. That
 * constraint is one good reason entities should not double as API types — see the
 * DTO package.
 *
 * <p>Full documentation: {@code docs/patterns/repository.md}
 */
@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    private int publishedYear;

    /** Required by JPA. Not for application use. */
    protected Book() {
    }

    public Book(String title, String author, int publishedYear) {
        this.title = title;
        this.author = author;
        this.publishedYear = publishedYear;
    }

    public Long getId() { return id; }

    public String getTitle() { return title; }

    public String getAuthor() { return author; }

    public int getPublishedYear() { return publishedYear; }

    @Override
    public String toString() {
        return title + " — " + author + " (" + publishedYear + ')';
    }
}
