package com.example.designpatterns.structural.proxy;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Asserts the two guarantees these proxies buy: the expensive object is not
 * created until it is genuinely needed, and forbidden calls never reach the
 * delegate at all.
 */
class ProxyTest {

    @Test
    void theVirtualProxyDefersConstructionUntilFirstUse() {
        AtomicInteger constructions = new AtomicInteger();
        LazyImageProxy proxy = new LazyImageProxy(() -> {
            constructions.incrementAndGet();
            return new RealImageService();
        });

        assertThat(constructions.get()).isZero();

        proxy.load("cat.png");
        proxy.load("dog.png");

        assertThat(constructions.get()).isEqualTo(1);
    }

    @Test
    void theProtectionProxyBlocksBeforeTheDelegateRuns() {
        AtomicInteger delegateCalls = new AtomicInteger();
        ImageService counting = id -> {
            delegateCalls.incrementAndGet();
            return new byte[] {1};
        };
        ImageService guarded = new ProtectedImageProxy(counting, Set.of("public.png"));

        assertThatThrownBy(() -> guarded.load("secret.png"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("secret.png");
        assertThat(delegateCalls.get()).isZero();

        guarded.load("public.png");
        assertThat(delegateCalls.get()).isEqualTo(1);
    }

    @Test
    void aProxyIsIndistinguishableFromTheSubjectToItsCaller() {
        ImageService real = new RealImageService();
        ImageService proxied = new LazyImageProxy(RealImageService::new);

        assertThat(proxied.load("cat.png")).isEqualTo(real.load("cat.png"));
    }
}
