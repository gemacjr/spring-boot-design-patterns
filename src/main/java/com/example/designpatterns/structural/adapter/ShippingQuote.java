package com.example.designpatterns.structural.adapter;

import java.math.BigDecimal;

/** The shape the application wants: typed, unit-explicit, and free of XML. */
public record ShippingQuote(String carrier, BigDecimal price, int estimatedDays) {
}
