package com.example.designpatterns.spring.repository;

/** The contract, run against the hand-written implementation. No database, no context. */
class InMemoryBookCatalogTest extends BookCatalogContractTest {

    @Override
    protected BookCatalog catalog() {
        return new InMemoryBookCatalog();
    }
}
