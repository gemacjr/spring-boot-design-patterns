package com.example.designpatterns.spring.dto;

/**
 * <b>Data Transfer Object</b>: the shape the API promises.
 *
 * <p>Three things it buys you:
 *
 * <ul>
 *   <li><b>Safety.</b> Fields that do not exist here cannot leak. Omission is
 *       enforced by the type, not by remembering to annotate.</li>
 *   <li><b>A stable contract.</b> Renaming an entity field does not break clients;
 *       the assembler absorbs the change.</li>
 *   <li><b>A shape suited to the caller.</b> {@code displayName} and a formatted
 *       {@code memberSince} are what a UI wants, not what a database row holds.</li>
 * </ul>
 *
 * <p>Full documentation: {@code docs/patterns/dto.md}
 */
public record CustomerDto(Long id, String displayName, String email, String memberSince) {
}
