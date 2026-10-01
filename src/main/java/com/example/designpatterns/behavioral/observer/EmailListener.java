package com.example.designpatterns.behavioral.observer;

import org.springframework.stereotype.Component;

/** <b>Concrete Observer</b>: notifies the customer. */
@Component
public class EmailListener extends RecordingListener {

    @Override
    public void onEvent(OrderEvent event) {
        record("EMAIL customer about " + event.type() + " on " + event.orderId());
    }
}
