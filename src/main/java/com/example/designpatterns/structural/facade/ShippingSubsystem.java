package com.example.designpatterns.structural.facade;

import java.util.UUID;

/** One of three subsystems the facade hides. */
public class ShippingSubsystem {

    public String schedule(String orderId, String address) {
        return "TRK-" + UUID.randomUUID().toString().substring(0, 8) + " to " + address;
    }
}
