package com.example.designpatterns.structural.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The point of this test: the bean Spring injected is <em>not</em> an instance of
 * the class you wrote. It is a generated proxy wrapping it. That single fact
 * explains {@code @Transactional}, {@code @Cacheable}, {@code @Async}, and the
 * classic "why doesn't my self-invocation get a transaction?" bug.
 */
@SpringBootTest
class AuditAspectTest {

    @Autowired AuditedService service;
    @Autowired AuditAspect aspect;

    @Test
    void springHandedUsAProxyNotTheRawClass() {
        assertThat(AopUtils.isAopProxy(service)).isTrue();
        assertThat(service.getClass()).isNotEqualTo(AuditedService.class);
    }

    @Test
    void theAspectObservesCallsWithoutTheServiceKnowingAboutIt() {
        aspect.clear();

        service.perform("reindex");

        assertThat(aspect.auditLog()).anyMatch(entry -> entry.contains("perform"));
    }

    @Test
    void selfInvocationBypassesTheProxy() {
        aspect.clear();

        service.performTwiceInternally("reindex");

        // Only the outer call goes through the proxy; the internal one does not.
        assertThat(aspect.auditLog()).hasSize(1);
    }
}
