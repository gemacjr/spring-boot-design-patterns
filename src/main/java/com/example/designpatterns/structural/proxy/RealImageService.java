package com.example.designpatterns.structural.proxy;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <b>Real Subject</b>: notionally expensive to construct and to call.
 *
 * <p>Pretend the constructor opens a decoder, memory-maps a cache, and warms a
 * thread pool. That imagined cost is the entire justification for the virtual
 * proxy sitting in front of it.
 */
public class RealImageService implements ImageService {

    private final AtomicInteger loads = new AtomicInteger();

    @Override
    public byte[] load(String id) {
        loads.incrementAndGet();
        return ("IMG:" + id).getBytes(StandardCharsets.UTF_8);
    }

    public int loadCount() {
        return loads.get();
    }
}
