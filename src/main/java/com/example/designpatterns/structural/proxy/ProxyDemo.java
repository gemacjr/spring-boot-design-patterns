package com.example.designpatterns.structural.proxy;

import com.example.designpatterns.catalog.Category;
import com.example.designpatterns.catalog.PatternDemo;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/** Catalog entry for the Proxy pattern. */
@Component
public class ProxyDemo implements PatternDemo {

    private final AuditedService auditedService;
    private final AuditAspect auditAspect;

    public ProxyDemo(AuditedService auditedService, AuditAspect auditAspect) {
        this.auditedService = auditedService;
        this.auditAspect = auditAspect;
    }

    @Override public String id() { return "proxy"; }
    @Override public Category category() { return Category.STRUCTURAL; }
    @Override public String title() { return "Proxy"; }

    @Override
    public String intent() {
        return "Provide a surrogate or placeholder for another object to control access to it.";
    }

    @Override
    public Object run() {
        Map<String, Object> result = new LinkedHashMap<>();

        LazyImageProxy lazy = new LazyImageProxy(RealImageService::new);
        result.put("virtualProxyLoadedBeforeUse", lazy.isLoaded());
        String bytes = new String(lazy.load("cat.png"), StandardCharsets.UTF_8);
        result.put("virtualProxyLoadedAfterUse", lazy.isLoaded());
        result.put("virtualProxyResult", bytes);

        ImageService guarded = new ProtectedImageProxy(new RealImageService(), Set.of("public.png"));
        result.put("protectionProxyAllowed",
                new String(guarded.load("public.png"), StandardCharsets.UTF_8));
        try {
            guarded.load("secret.png");
            result.put("protectionProxyDenied", "unexpectedly allowed");
        } catch (IllegalArgumentException expected) {
            result.put("protectionProxyDenied", expected.getMessage());
        }

        auditAspect.clear();
        auditedService.perform("reindex");
        result.put("springProxyClass", auditedService.getClass().getName());
        result.put("springIsAopProxy", AopUtils.isAopProxy(auditedService));
        result.put("springAuditLog", auditAspect.auditLog());
        result.put("note", "The injected AuditedService is a generated subclass, not your class. "
                + "That is how @Transactional works — and why self-invocation skips it.");
        return result;
    }
}
