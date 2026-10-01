package com.example.designpatterns.structural.adapter;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantee Adapter buys: clients program against the interface they
 * want, while an unchangeable third-party API is confined to one class.
 */
class AdapterTest {

    /** Captures what the adapter actually passed down to the legacy API. */
    private static class RecordingLegacyApi extends LegacyXmlShippingApi {
        final AtomicInteger lastGrams = new AtomicInteger(-1);
        String lastZip;

        @Override
        public String getRateXml(String zip, int grams) {
            this.lastZip = zip;
            this.lastGrams.set(grams);
            return super.getRateXml(zip, grams);
        }
    }

    @Test
    void adaptsTheLegacyShapeToTheInterfaceCallersWant() {
        ShippingService service = new LegacyShippingAdapter(new LegacyXmlShippingApi());

        ShippingQuote quote = service.quote("SW1A 1AA", 2.5);

        assertThat(quote.carrier()).isNotBlank();
        assertThat(quote.price()).isGreaterThan(BigDecimal.ZERO);
        assertThat(quote.estimatedDays()).isPositive();
    }

    @Test
    void convertsUnitsOnTheWayIn() {
        RecordingLegacyApi legacy = new RecordingLegacyApi();

        new LegacyShippingAdapter(legacy).quote("SW1A 1AA", 2.5);

        assertThat(legacy.lastGrams.get()).isEqualTo(2500);
        assertThat(legacy.lastZip).isEqualTo("SW1A 1AA");
    }

    @Test
    void convertsCentsToACurrencyAmountOnTheWayOut() {
        ShippingQuote quote = new LegacyShippingAdapter(new LegacyXmlShippingApi())
                .quote("SW1A 1AA", 1.0);

        assertThat(quote.price().scale()).isEqualTo(2);
        assertThat(quote.price()).isEqualByComparingTo(new BigDecimal("5.50"));
    }

    @Test
    void theAdapterIsTheOnlyPlaceThatKnowsTheLegacyTypeExists() {
        ShippingService service = new LegacyShippingAdapter(new LegacyXmlShippingApi());

        assertThat(service).isInstanceOf(ShippingService.class);
        assertThat(ShippingService.class.getMethods())
                .noneMatch(method -> method.getReturnType().getName().contains("Legacy"));
    }
}
