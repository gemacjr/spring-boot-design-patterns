package com.example.designpatterns.spring.dto;

import java.time.LocalDate;

/**
 * Stands in for a persistence entity.
 *
 * <p>It carries fields no API client should ever see: {@code passwordHash} and
 * {@code internalNotes}. Returning this object from a controller would serialise
 * both, and nothing in the type system would warn you.
 *
 * <p>(It is a plain class rather than a real {@code @Entity} so this package stays
 * free of JPA — only the repository package touches the database. The shape is
 * what matters here.)
 *
 * <p>Full documentation: {@code docs/patterns/dto.md}
 */
public class CustomerEntity {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String passwordHash;
    private String internalNotes;
    private LocalDate joinedOn;

    public CustomerEntity(Long id, String firstName, String lastName, String email,
                          String passwordHash, String internalNotes, LocalDate joinedOn) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.internalNotes = internalNotes;
        this.joinedOn = joinedOn;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getInternalNotes() { return internalNotes; }
    public LocalDate getJoinedOn() { return joinedOn; }
}
