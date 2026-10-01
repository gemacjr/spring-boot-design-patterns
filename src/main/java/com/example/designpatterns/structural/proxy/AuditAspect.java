package com.example.designpatterns.structural.proxy;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Spring's own Proxy pattern, made visible.
 *
 * <p>At startup Spring notices this pointcut matches {@link AuditedService},
 * generates a proxy class implementing the same public surface, and registers the
 * proxy as the bean. Every injected reference points at the proxy.
 *
 * <p>Full documentation: {@code docs/patterns/proxy.md}
 */
@Aspect
@Component
public class AuditAspect {

    private final List<String> auditLog = new CopyOnWriteArrayList<>();

    @Before("execution(* com.example.designpatterns.structural.proxy.AuditedService.*(..))")
    public void record(JoinPoint joinPoint) {
        auditLog.add("called " + joinPoint.getSignature().getName()
                + " with " + java.util.Arrays.toString(joinPoint.getArgs()));
    }

    public List<String> auditLog() {
        return List.copyOf(auditLog);
    }

    public void clear() {
        auditLog.clear();
    }
}
