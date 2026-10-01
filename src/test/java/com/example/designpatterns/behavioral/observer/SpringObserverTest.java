package com.example.designpatterns.behavioral.observer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/** The framework-native form of the same pattern. */
@SpringBootTest
class SpringObserverTest {

    @Autowired SpringOrderEvents events;
    @Autowired SpringEventRecorder recorder;

    @Test
    void annotatedListenersReceivePublishedEvents() {
        recorder.clear();

        events.placeOrder("ORD-42");

        assertThat(recorder.seen()).hasSize(1);
        assertThat(recorder.seen().get(0).orderId()).isEqualTo("ORD-42");
        assertThat(recorder.seen().get(0).type()).isEqualTo("PLACED");
    }
}
