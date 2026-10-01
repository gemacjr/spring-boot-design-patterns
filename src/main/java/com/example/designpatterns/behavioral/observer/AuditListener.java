package com.example.designpatterns.behavioral.observer;

import org.springframework.stereotype.Component;

/** <b>Concrete Observer</b>: writes an audit trail. */
@Component
public class AuditListener extends RecordingListener {

    @Override
    public void onEvent(OrderEvent event) {
        record("AUDIT " + event.type() + " " + event.orderId());
    }
}
