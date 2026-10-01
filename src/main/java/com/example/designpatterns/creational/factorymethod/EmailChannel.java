package com.example.designpatterns.creational.factorymethod;

/** <b>Concrete Product</b>: delivery over email. */
public class EmailChannel implements NotificationChannel {

    @Override
    public String send(String to, String message) {
        return "EMAIL to " + to + ": " + message;
    }
}
