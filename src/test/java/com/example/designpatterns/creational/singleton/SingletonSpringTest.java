package com.example.designpatterns.creational.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The contrast that matters: a {@code @Component} is a singleton <em>per
 * application context</em>, not per JVM, and it is a perfectly ordinary object
 * you can still construct with {@code new} in a unit test.
 */
@SpringBootTest
class SingletonSpringTest {

    @Autowired ApplicationContext context;
    @Autowired CounterBean injectedOnce;
    @Autowired CounterBean injectedTwice;

    @Test
    void springHandsOutTheSameBeanInstanceEveryTime() {
        assertThat(injectedOnce).isSameAs(injectedTwice);
        assertThat(context.getBean(CounterBean.class)).isSameAs(injectedOnce);
    }

    @Test
    void theBeanIsStillAnOrdinaryObjectUnlikeAClassicSingleton() {
        CounterBean standalone = new CounterBean();

        standalone.increment();

        assertThat(standalone.count()).isEqualTo(1);
        assertThat(standalone).isNotSameAs(injectedOnce);
    }
}
