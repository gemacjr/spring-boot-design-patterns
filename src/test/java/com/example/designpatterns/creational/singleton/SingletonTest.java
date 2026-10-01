package com.example.designpatterns.creational.singleton;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Asserts the guarantee Singleton buys: exactly one instance, ever, even under
 * concurrent first access.
 *
 * <p>Note the assertions are written as deltas rather than absolutes. A real
 * singleton carries state across every test in the JVM, which is precisely the
 * testability cost the docs warn about.
 */
class SingletonTest {

    @Test
    void alwaysReturnsTheIdenticalInstance() {
        assertThat(RequestCounter.getInstance()).isSameAs(RequestCounter.getInstance());
    }

    @Test
    void onlyOneInstanceIsCreatedUnderConcurrentFirstAccess() throws Exception {
        int threads = 16;
        Set<RequestCounter> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            new Thread(() -> {
                try {
                    start.await();
                    RequestCounter instance = RequestCounter.getInstance();
                    synchronized (seen) {
                        seen.add(instance);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    done.countDown();
                }
            }).start();
        }

        start.countDown();
        assertThat(done.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(seen).hasSize(1);
    }

    @Test
    void stateAccumulatesAcrossUnrelatedCallers() {
        long before = RequestCounter.getInstance().count();

        RequestCounter.getInstance().increment();
        RequestCounter.getInstance().increment();
        RequestCounter.getInstance().increment();

        assertThat(RequestCounter.getInstance().count()).isEqualTo(before + 3);
    }
}
