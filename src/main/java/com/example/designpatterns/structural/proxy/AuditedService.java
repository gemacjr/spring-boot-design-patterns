package com.example.designpatterns.structural.proxy;

import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * A perfectly ordinary service — no auditing code anywhere in it.
 *
 * <p>Auditing is added from outside by {@link AuditAspect}. Spring achieves that
 * by handing every injection point a generated <em>proxy</em> of this class rather
 * than an instance of it. This is exactly the mechanism behind
 * {@code @Transactional}, {@code @Cacheable} and {@code @Async}.
 *
 * <p>Full documentation: {@code docs/patterns/proxy.md}
 */
@Service
public class AuditedService {

    public String perform(String action) {
        return "performed " + action.toUpperCase(Locale.ROOT);
    }

    /**
     * Calls {@link #perform} on {@code this}, not through the proxy.
     *
     * <p>This is the notorious self-invocation trap: the inner calls bypass the
     * proxy entirely, so no advice runs for them. The same trap silently drops
     * {@code @Transactional} on internal calls.
     */
    public String performTwiceInternally(String action) {
        return perform(action) + " / " + perform(action);
    }
}
