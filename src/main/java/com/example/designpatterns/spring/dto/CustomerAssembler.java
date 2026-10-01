package com.example.designpatterns.spring.dto;

import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <b>Assembler</b>: the single place entities become DTOs.
 *
 * <p>Concentrating the mapping here means there is exactly one place to audit when
 * asking "can this field reach a client?". Scattering {@code new CustomerDto(...)}
 * across controllers makes that question unanswerable.
 *
 * <p>Full documentation: {@code docs/patterns/dto.md}
 */
@Component
public class CustomerAssembler {

    private static final DateTimeFormatter MONTH_AND_YEAR = DateTimeFormatter.ofPattern("MMMM yyyy");

    public CustomerDto toDto(CustomerEntity entity) {
        return new CustomerDto(
                entity.getId(),
                entity.getFirstName() + " " + entity.getLastName(),
                entity.getEmail(),
                entity.getJoinedOn().format(MONTH_AND_YEAR));
    }

    public List<CustomerDto> toDtoList(List<CustomerEntity> entities) {
        return entities.stream().map(this::toDto).toList();
    }
}
