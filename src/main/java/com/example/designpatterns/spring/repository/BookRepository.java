package com.example.designpatterns.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data's version: an interface with no implementation you ever write.
 *
 * <p>At startup Spring Data generates a proxy implementing this interface, parsing
 * the <em>method name</em> {@code findByAuthorIgnoreCase} into a query. This is
 * Repository plus Proxy plus a little Interpreter, and it is why you can delete a
 * hand-written DAO layer entirely.
 *
 * <p>The trade-off worth knowing: the query lives in a method name, so a typo is a
 * startup failure rather than a compile error, and complex queries outgrow the
 * derivation rules quickly (at which point you write {@code @Query}).
 *
 * <p>Full documentation: {@code docs/patterns/repository.md}
 */
public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAuthorIgnoreCase(String author);
}
