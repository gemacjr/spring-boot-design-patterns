package com.example.designpatterns.structural.adapter;

import java.math.BigDecimal;

/**
 * <b>Adapter</b>: implements {@link ShippingService} in terms of
 * {@link LegacyXmlShippingApi}.
 *
 * <p>This is an <em>object</em> adapter — it holds the adaptee rather than
 * extending it. Prefer this to the class adapter (inheritance) form: composition
 * keeps the legacy API's methods off your public surface, and works even when the
 * adaptee is {@code final}.
 *
 * <p>Everything ugly happens here: kilograms become grams, XML becomes a record,
 * integer cents become a two-decimal amount. One class absorbs the impedance
 * mismatch so that no other class has to know it exists.
 *
 * <p>Full documentation: {@code docs/patterns/adapter.md}
 */
public class LegacyShippingAdapter implements ShippingService {

    private final LegacyXmlShippingApi legacy;

    public LegacyShippingAdapter(LegacyXmlShippingApi legacy) {
        this.legacy = legacy;
    }

    @Override
    public ShippingQuote quote(String postcode, double weightKg) {
        int grams = (int) Math.round(weightKg * 1000);
        String xml = legacy.getRateXml(postcode, grams);

        return new ShippingQuote(
                tag(xml, "carrier"),
                BigDecimal.valueOf(Long.parseLong(tag(xml, "cents")), 2),
                Integer.parseInt(tag(xml, "days")));
    }

    /** Deliberately simple extraction; a real adapter would use a proper parser. */
    private static String tag(String xml, String name) {
        String open = "<" + name + ">";
        String close = "</" + name + ">";
        int start = xml.indexOf(open) + open.length();
        int end = xml.indexOf(close);
        if (start < open.length() || end < 0) {
            throw new IllegalStateException("legacy response missing <" + name + ">: " + xml);
        }
        return xml.substring(start, end);
    }
}
