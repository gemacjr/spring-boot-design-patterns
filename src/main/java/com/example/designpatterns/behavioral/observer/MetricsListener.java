package com.example.designpatterns.behavioral.observer;

import org.springframework.stereotype.Component;

/** <b>Concrete Observer</b>: increments a counter. */
@Component
public class MetricsListener extends RecordingListener {

    @Override
    public void onEvent(OrderEvent event) {
        record("METRIC orders." + event.type().toLowerCase());
    }
}
