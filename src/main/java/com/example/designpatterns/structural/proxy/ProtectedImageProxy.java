package com.example.designpatterns.structural.proxy;

import java.util.Set;

/**
 * <b>Protection Proxy</b>: enforces an access rule in front of the real subject.
 *
 * <p>The check happens <em>before</em> delegation, so a forbidden call never
 * reaches the delegate at all. Putting the rule in a proxy rather than inside the
 * service keeps authorisation policy separate from the work being authorised.
 *
 * <p>Full documentation: {@code docs/patterns/proxy.md}
 */
public class ProtectedImageProxy implements ImageService {

    private final ImageService delegate;
    private final Set<String> allowedIds;

    public ProtectedImageProxy(ImageService delegate, Set<String> allowedIds) {
        this.delegate = delegate;
        this.allowedIds = Set.copyOf(allowedIds);
    }

    @Override
    public byte[] load(String id) {
        if (!allowedIds.contains(id)) {
            throw new IllegalArgumentException("access denied for image '" + id + "'");
        }
        return delegate.load(id);
    }
}
