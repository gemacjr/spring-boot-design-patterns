package com.example.designpatterns.structural.proxy;

/**
 * <b>Subject</b>: the interface shared by the real object and every proxy of it.
 *
 * <p>A proxy is only transparent because it implements this same type. That is
 * the difference between Proxy and Decorator in practice: both wrap, but a
 * decorator advertises added behaviour while a proxy pretends to be the thing.
 *
 * <p>Full documentation: {@code docs/patterns/proxy.md}
 */
@FunctionalInterface
public interface ImageService {

    byte[] load(String id);
}
