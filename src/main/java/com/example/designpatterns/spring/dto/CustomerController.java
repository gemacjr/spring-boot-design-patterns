package com.example.designpatterns.spring.dto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Note the return type: {@code List<CustomerDto>}, never {@code List<CustomerEntity>}.
 *
 * <pre>
 * curl -s localhost:8080/api/patterns/dto/customers
 * </pre>
 */
@RestController
@RequestMapping("/api/patterns/dto")
public class CustomerController {

    static final List<CustomerEntity> SAMPLE = List.of(
            new CustomerEntity(1L, "Ada", "Lovelace", "ada@example.com",
                    "$2a$10$notarealhashatall", "Chased us for an invoice in March.",
                    LocalDate.of(2019, 3, 14)),
            new CustomerEntity(2L, "Grace", "Hopper", "grace@example.com",
                    "$2a$10$alsonotarealhash", "VIP — route to senior support.",
                    LocalDate.of(2021, 11, 2)));

    private final CustomerAssembler assembler;

    public CustomerController(CustomerAssembler assembler) {
        this.assembler = assembler;
    }

    @GetMapping("/customers")
    public List<CustomerDto> customers() {
        return assembler.toDtoList(SAMPLE);
    }
}
