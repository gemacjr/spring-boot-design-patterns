package com.example.designpatterns.structural.facade;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Drive the facade directly, including its failure path.
 *
 * <pre>
 * curl -s -X POST 'localhost:8080/api/patterns/facade/checkout?sku=WIDGET-1&amp;quantity=2&amp;card=4111111111111111&amp;address=London'
 * curl -s -X POST 'localhost:8080/api/patterns/facade/checkout?sku=WIDGET-1&amp;quantity=2&amp;card=FAIL-CARD&amp;address=London'
 * </pre>
 */
@RestController
@RequestMapping("/api/patterns/facade")
public class FacadeController {

    @PostMapping("/checkout")
    public CheckoutResult checkout(@RequestParam String sku,
                                   @RequestParam int quantity,
                                   @RequestParam String card,
                                   @RequestParam String address) {
        return new CheckoutFacade().checkout(sku, quantity, card, address);
    }
}
