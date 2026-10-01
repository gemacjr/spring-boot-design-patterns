package com.example.designpatterns.structural.adapter;

/**
 * <b>Adaptee</b>: a third-party API you cannot change.
 *
 * <p>It speaks grams and returns a string of XML. In real life this would be a
 * vendor JAR or a SOAP stub. The whole point of the pattern is that this
 * awkwardness is quarantined here and in the adapter, and nowhere else.
 *
 * <p>Full documentation: {@code docs/patterns/adapter.md}
 */
public class LegacyXmlShippingApi {

    /**
     * @param zip   destination postcode
     * @param grams parcel weight in grams — note the unit differs from the domain's
     * @return an XML document as a string
     */
    public String getRateXml(String zip, int grams) {
        int cents = 300 + (grams / 4);
        int days = 2 + (grams / 2000);
        return "<rate>"
                + "<carrier>OLDPOST</carrier>"
                + "<zip>" + zip + "</zip>"
                + "<cents>" + cents + "</cents>"
                + "<days>" + days + "</days>"
                + "</rate>";
    }
}
