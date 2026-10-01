package com.example.designpatterns.creational.factorymethod;

/** <b>Concrete Product</b>: delivery over SMS. */
public class SmsChannel implements NotificationChannel {

    @Override
    public String send(String to, String message) {
        return "SMS to " + to + ": " + message;
    }
}
