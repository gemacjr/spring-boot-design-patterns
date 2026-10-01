package com.example.designpatterns.creational.factorymethod;

/** <b>Concrete Creator</b>: produces an {@link EmailChannel}. */
public class EmailDispatcher extends NotificationDispatcher {

    @Override
    protected NotificationChannel createChannel() {
        return new EmailChannel();
    }
}
