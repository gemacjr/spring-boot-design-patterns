package com.example.designpatterns.structural.decorator;

/** <b>Concrete Component</b>: the undecorated behaviour. */
public class PlainMessageSender implements MessageSender {

    @Override
    public String send(String message) {
        return "sent:" + message;
    }
}
