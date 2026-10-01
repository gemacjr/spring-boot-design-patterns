package com.example.designpatterns.creational.factorymethod;

/** <b>Concrete Creator</b>: produces an {@link SmsChannel}. */
public class SmsDispatcher extends NotificationDispatcher {

    @Override
    protected NotificationChannel createChannel() {
        return new SmsChannel();
    }
}
