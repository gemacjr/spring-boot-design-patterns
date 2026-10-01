package com.example.designpatterns.creational.builder;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lets you drive the builder from outside and watch its validation reject bad input.
 *
 * <p>Try it:
 * <pre>
 * curl -s -X POST localhost:8080/api/patterns/builder/orders \
 *   -H 'Content-Type: application/json' \
 *   -d '{"customer":"Ada","shipTo":"London","expedited":true,
 *        "lines":[{"sku":"W-1","quantity":2,"unitPrice":10.00}]}'
 * </pre>
 */
@RestController
@RequestMapping("/api/patterns/builder")
public class BuilderController {

    /** Inbound shape. Deliberately separate from {@link OrderRequest} — see the DTO pattern. */
    public record LineRequest(String sku, int quantity, BigDecimal unitPrice) {
    }

    public record OrderRequestBody(String customer, String shipTo, boolean expedited,
                                   List<LineRequest> lines) {
    }

    @PostMapping("/orders")
    public Map<String, Object> create(@RequestBody OrderRequestBody body) {
        OrderRequest.Builder builder = OrderRequest.builder()
                .customer(body.customer())
                .shipTo(body.shipTo())
                .expedited(body.expedited());

        if (body.lines() != null) {
            body.lines().forEach(line -> builder.addLine(line.sku(), line.quantity(), line.unitPrice()));
        }

        OrderRequest order = builder.build();

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("customer", order.customer());
        result.put("lines", order.lines());
        result.put("total", order.total());
        result.put("expedited", order.expedited());
        return result;
    }
}
