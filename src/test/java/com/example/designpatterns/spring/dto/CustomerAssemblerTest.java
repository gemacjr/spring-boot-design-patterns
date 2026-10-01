package com.example.designpatterns.spring.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantee the DTO buys: sensitive fields cannot reach a client.
 *
 * <p>The decisive test asserts on the <em>serialised JSON keys</em>, not on the
 * record's declaration. Asserting the record shape would pass trivially and prove
 * nothing; asserting the JSON proves what a caller actually receives.
 */
class CustomerAssemblerTest {

    private final CustomerAssembler assembler = new CustomerAssembler();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private CustomerEntity entity() {
        return new CustomerEntity(1L, "Ada", "Lovelace", "ada@example.com",
                "$2a$10$notarealhashatall", "Chased us for an invoice in March.",
                LocalDate.of(2019, 3, 14));
    }

    @Test
    void theSerialisedDtoContainsNoSensitiveFields() throws Exception {
        String json = objectMapper.writeValueAsString(assembler.toDto(entity()));

        assertThat(json).doesNotContain("passwordHash")
                .doesNotContain("notarealhash")
                .doesNotContain("internalNotes")
                .doesNotContain("Chased us");
    }

    @Test
    void theSerialisedDtoExposesExactlyTheAgreedKeys() throws Exception {
        String json = objectMapper.writeValueAsString(assembler.toDto(entity()));

        @SuppressWarnings("unchecked")
        var keys = objectMapper.readValue(json, java.util.Map.class).keySet();

        assertThat(keys).containsExactlyInAnyOrder("id", "displayName", "email", "memberSince");
    }

    @Test
    void theAssemblerReshapesRatherThanMerelyCopying() {
        CustomerDto dto = assembler.toDto(entity());

        assertThat(dto.displayName()).isEqualTo("Ada Lovelace");
        assertThat(dto.memberSince()).isEqualTo("March 2019");
        assertThat(dto.email()).isEqualTo("ada@example.com");
    }

    @Test
    void theEntityStillHoldsTheFieldsTheDtoOmits() {
        // Proving the omission is the DTO's doing, not missing data.
        assertThat(entity().getPasswordHash()).isNotBlank();
        assertThat(entity().getInternalNotes()).isNotBlank();
    }

    @Test
    void listsAreMappedElementwise() {
        List<CustomerDto> dtos = assembler.toDtoList(CustomerController.SAMPLE);

        assertThat(dtos).hasSize(2);
        assertThat(dtos).extracting(CustomerDto::displayName)
                .containsExactly("Ada Lovelace", "Grace Hopper");
    }
}
