package com.example.designpatterns.structural.proxy;

import java.util.function.Supplier;

/**
 * <b>Virtual Proxy</b>: defers creating the real subject until it is first used.
 *
 * <p>Callers cannot tell the difference — they hold an {@link ImageService} either
 * way — which is what makes the optimisation safe to apply after the fact.
 *
 * <p>Double-checked locking with a {@code volatile} field makes first access
 * thread-safe without paying for a lock on every subsequent call.
 *
 * <p>Full documentation: {@code docs/patterns/proxy.md}
 */
public class LazyImageProxy implements ImageService {

    private final Supplier<ImageService> factory;
    private volatile ImageService realSubject;

    public LazyImageProxy(Supplier<ImageService> factory) {
        this.factory = factory;
    }

    @Override
    public byte[] load(String id) {
        return realSubject().load(id);
    }

    /** @return whether the expensive object has been created yet */
    public boolean isLoaded() {
        return realSubject != null;
    }

    private ImageService realSubject() {
        ImageService local = realSubject;
        if (local == null) {
            synchronized (this) {
                local = realSubject;
                if (local == null) {
                    local = factory.get();
                    realSubject = local;
                }
            }
        }
        return local;
    }
}
