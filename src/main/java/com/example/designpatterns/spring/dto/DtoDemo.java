package com.example.designpatterns.spring.dto;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/** Catalog entry for the DTO / Assembler pattern. */
@Component
public class DtoDemo implements PatternDemo {

    private final CustomerAssembler assembler;
    private final ObjectMapper objectMapper;

    public DtoDemo(CustomerAssembler assembler, ObjectMapper objectMapper) {
        this.assembler = assembler;
        this.objectMapper = objectMapper;
    }

    @Override public String id() { return "dto"; }
    @Override public Category category() { return Category.SPRING; }
    @Override public String title() { return "DTO / Assembler"; }

    @Override
    public String intent() {
        return "Carry data across a process or API boundary in a shape owned by that boundary, "
                + "decoupling the wire contract from the internal model.";
    }

    @Override
    public Object run() {
        CustomerEntity entity = CustomerController.SAMPLE.get(0);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("whatTheEntityHolds", entityFieldNames());
        result.put("whatTheApiExposes", serialise(assembler.toDto(entity)));
        result.put("wouldHaveLeaked", java.util.List.of("passwordHash", "internalNotes"));
        result.put("note", "The DTO has no field for the password hash or the internal notes, so "
                + "they cannot leak by accident. Omission is enforced by the type.");
        return result;
    }

    private java.util.List<String> entityFieldNames() {
        return java.util.Arrays.stream(CustomerEntity.class.getDeclaredFields())
                .map(java.lang.reflect.Field::getName)
                .toList();
    }

    private Object serialise(CustomerDto dto) {
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(dto), Map.class);
        } catch (Exception e) {
            throw new IllegalStateException("could not serialise DTO", e);
        }
    }
}
