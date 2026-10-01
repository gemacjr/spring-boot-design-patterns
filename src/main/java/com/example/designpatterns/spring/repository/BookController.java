package com.example.designpatterns.spring.repository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <pre>
 * curl -s 'localhost:8080/api/patterns/repository/books'
 * curl -s 'localhost:8080/api/patterns/repository/books?author=ursula%20k.%20le%20guin'
 * </pre>
 */
@RestController
@RequestMapping("/api/patterns/repository")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/books")
    public List<BookView> books(@RequestParam(required = false) String author) {
        List<Book> books = author == null ? bookService.all() : bookService.byAuthor(author);
        return books.stream().map(BookView::of).toList();
    }
}
