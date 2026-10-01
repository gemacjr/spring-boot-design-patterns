package com.example.designpatterns.structural.decorator;

import java.util.List;

/**
 * <b>Concrete Decorator</b>: records what passes through it.
 *
 * <p>The log is injected rather than written to a static logger so the test can
 * observe it — and so the decorator stays free of hidden global state.
 */
public class LoggingSender extends MessageSenderDecorator {

    private final List<String> log;

    public LoggingSender(MessageSender delegate, List<String> log) {
        super(delegate);
        this.log = log;
    }

    @Override
    public String send(String message) {
        log.add("sending: " + message);
        String result = delegate.send(message);
        log.add("result: " + result);
        return result;
    }
}
