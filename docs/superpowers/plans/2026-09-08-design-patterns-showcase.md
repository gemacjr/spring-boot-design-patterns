# Design Patterns Showcase Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build a runnable Spring Boot application that teaches 19 design patterns, each in an isolated package with a working implementation, REST endpoint, behavioural test, and documentation page.

**Architecture:** A thin `catalog` spine defines a `PatternDemo` contract; Spring collects every implementation into a `PatternCatalog` registry exposed at `/api/patterns`. Each of the 19 patterns lives in its own leaf package under `creational/`, `structural/`, `behavioral/`, or `spring/`, depends on nothing but the catalog contract, and is independently completable.

**Tech Stack:** Java 17 (Corretto), Gradle 9.6 Kotlin DSL, Spring Boot 3.5.16, Spring Web, Spring Data JPA, H2 (in-memory), Spring AOP, JUnit 5 via `spring-boot-starter-test`, MockMvc.

**Spec:** `docs/superpowers/specs/2026-09-08-design-patterns-showcase-design.md`

## Global Constraints

- Spring Boot version is **3.5.16** exactly. Do NOT use 4.x — it requires Java 21 and the toolchain is Java 17.
- Java toolchain is **17**. Records, sealed interfaces, and pattern-matching `switch` are available; virtual threads and `Boot 4` APIs are not.
- Root package is **`com.example.designpatterns`**. Every class lives under it.
- Build file is **`build.gradle.kts`** (Kotlin DSL). The existing Gradle wrapper (9.6.0) is retained; do not regenerate it.
- Every leaf pattern package exposes **exactly one** `@Component` implementing `PatternDemo`, whose `id()` is the lowercase package name.
- A pattern package may depend on `catalog/` and the JDK only. **No leaf pattern package may import another leaf pattern package.** This isolation is the point of the whole layout.
- Only the `spring/repository` package may touch JPA, `@Entity`, or the database.
- Every public type carries class-level Javadoc naming its role in the pattern (e.g. "Concrete Strategy") and linking to `docs/patterns/<id>.md`.
- Documentation is written once, in `docs/patterns/<id>.md`. Package `README.md` files are three-line stubs that link there — never copies.
- Tests assert the *property the pattern provides*, not merely that code runs.
- **Git:** the repository is not yet initialised. Task 0 handles this. Until it is done, skip the commit step of each task.

---

### Task 0: Initialise the repository

**Files:**
- Modify: `.gitignore` (verify it covers `build/`, `.gradle/`, `.idea/`)

- [ ] **Step 1: Confirm with the user that `git init` is wanted.** If they decline, skip this task entirely and skip every "Commit" step in this plan.

- [ ] **Step 2: Initialise and make the baseline commit**

```bash
git init
git add .
git commit -m "chore: baseline Gradle project before Spring Boot conversion"
```

---

### Task 1: Convert the build to Spring Boot and boot the application

**Files:**
- Modify: `build.gradle.kts` (entire file replaced)
- Create: `src/main/java/com/example/designpatterns/DesignPatternsApplication.java`
- Create: `src/main/resources/application.yml`
- Test: `src/test/java/com/example/designpatterns/DesignPatternsApplicationTests.java`

**Interfaces:**
- Consumes: nothing.
- Produces: a bootable Spring context. Every later task relies on `DesignPatternsApplication` as the `@SpringBootApplication` entry point at package root, so component scanning reaches all sub-packages.

- [ ] **Step 1: Replace `build.gradle.kts`**

```kotlin
plugins {
    java
    id("org.springframework.boot") version "3.5.16"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    runtimeOnly("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}
```

- [ ] **Step 2: Create `application.yml`**

```yaml
spring:
  application:
    name: design-patterns-showcase
  datasource:
    url: jdbc:h2:mem:patterns;DB_CLOSE_DELAY=-1
    driver-class-name: org.h2.Driver
    username: sa
    password: ""
  jpa:
    hibernate:
      ddl-auto: create-drop
    open-in-view: false
  h2:
    console:
      enabled: true
      path: /h2-console
logging:
  level:
    com.example.designpatterns: DEBUG
```

- [ ] **Step 3: Write the failing context test**

```java
package com.example.designpatterns;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DesignPatternsApplicationTests {

    @Test
    void contextLoads() {
    }
}
```

- [ ] **Step 4: Run it and verify it fails**

Run: `./gradlew test --tests '*DesignPatternsApplicationTests*'`
Expected: FAIL — no `@SpringBootConfiguration` found, because the application class does not exist yet.

- [ ] **Step 5: Create the application class**

```java
package com.example.designpatterns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point for the design patterns showcase.
 *
 * <p>Lives at the root package so component scanning reaches every pattern
 * package beneath it.
 */
@SpringBootApplication
public class DesignPatternsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DesignPatternsApplication.class, args);
    }
}
```

- [ ] **Step 6: Run the test and verify it passes**

Run: `./gradlew test --tests '*DesignPatternsApplicationTests*'`
Expected: PASS

- [ ] **Step 7: Verify the app actually boots**

Run: `./gradlew bootRun` — expect "Started DesignPatternsApplication" on port 8080, then stop it.

- [ ] **Step 8: Commit**

```bash
git add build.gradle.kts src/main src/test
git commit -m "feat: convert build to Spring Boot 3.5.16 and add application entry point"
```

---

### Task 2: Build the catalog spine

**Files:**
- Create: `src/main/java/com/example/designpatterns/catalog/Category.java`
- Create: `src/main/java/com/example/designpatterns/catalog/PatternDemo.java`
- Create: `src/main/java/com/example/designpatterns/catalog/PatternSummary.java`
- Create: `src/main/java/com/example/designpatterns/catalog/PatternCatalog.java`
- Create: `src/main/java/com/example/designpatterns/catalog/CatalogController.java`
- Create: `src/main/java/com/example/designpatterns/catalog/UnknownPatternException.java`
- Create: `src/main/java/com/example/designpatterns/catalog/CatalogExceptionHandler.java`
- Test: `src/test/java/com/example/designpatterns/catalog/PatternCatalogTest.java`

**Interfaces:**
- Consumes: the Spring context from Task 1.
- Produces — **every later task depends on these exact signatures:**
  - `enum Category { CREATIONAL, STRUCTURAL, BEHAVIORAL, SPRING }`
  - `interface PatternDemo { String id(); Category category(); String title(); String intent(); Object run(); }`
  - `record PatternSummary(String id, Category category, String title, String intent, String docsUrl, String runUrl)`
  - `PatternCatalog.findById(String id) -> PatternDemo` (throws `UnknownPatternException`)
  - `PatternCatalog.summaries() -> List<PatternSummary>`
  - `UnknownPatternException(String id, Collection<String> knownIds)`

- [ ] **Step 1: Write the failing catalog test**

```java
package com.example.designpatterns.catalog;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PatternCatalogTest {

    /** A stand-in demo so the catalog can be tested without any real pattern package. */
    private record FakeDemo(String id, Category category) implements PatternDemo {
        public String title() { return "Fake " + id; }
        public String intent() { return "intent of " + id; }
        public Object run() { return "ran " + id; }
    }

    @Test
    void indexesEveryRegisteredDemoById() {
        PatternCatalog catalog = new PatternCatalog(
                List.of(new FakeDemo("strategy", Category.BEHAVIORAL),
                        new FakeDemo("builder", Category.CREATIONAL)));

        assertThat(catalog.findById("strategy").title()).isEqualTo("Fake strategy");
        assertThat(catalog.summaries()).hasSize(2);
    }

    @Test
    void rejectsDuplicateIdsAtConstruction() {
        assertThatThrownBy(() -> new PatternCatalog(
                List.of(new FakeDemo("strategy", Category.BEHAVIORAL),
                        new FakeDemo("strategy", Category.STRUCTURAL))))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("strategy");
    }

    @Test
    void unknownIdReportsTheValidOnes() {
        PatternCatalog catalog = new PatternCatalog(List.of(new FakeDemo("strategy", Category.BEHAVIORAL)));

        assertThatThrownBy(() -> catalog.findById("nope"))
                .isInstanceOf(UnknownPatternException.class)
                .hasMessageContaining("strategy");
    }

    @Test
    void summaryCarriesDocsAndRunLinks() {
        PatternCatalog catalog = new PatternCatalog(List.of(new FakeDemo("strategy", Category.BEHAVIORAL)));

        PatternSummary summary = catalog.summaries().get(0);
        assertThat(summary.docsUrl()).isEqualTo("docs/patterns/strategy.md");
        assertThat(summary.runUrl()).isEqualTo("/api/patterns/strategy");
    }
}
```

- [ ] **Step 2: Run it and verify it fails**

Run: `./gradlew test --tests '*PatternCatalogTest*'`
Expected: FAIL — compilation error, `PatternCatalog` does not exist.

- [ ] **Step 3: Write `Category`, `PatternDemo`, `PatternSummary`**

```java
package com.example.designpatterns.catalog;

/** The four families this showcase groups patterns into. */
public enum Category { CREATIONAL, STRUCTURAL, BEHAVIORAL, SPRING }
```

```java
package com.example.designpatterns.catalog;

/**
 * Contract every pattern package implements exactly once.
 *
 * <p>This interface plus {@link PatternCatalog} is itself a Strategy and a
 * Registry: the container collects all implementations and the catalog
 * dispatches by id without knowing any concrete type.
 */
public interface PatternDemo {

    /** Stable identifier, equal to the lowercase leaf package name, e.g. "strategy". */
    String id();

    Category category();

    /** Human-readable name, e.g. "Strategy". */
    String title();

    /** The one-line Gang of Four intent. */
    String intent();

    /** Runs a canned demonstration and returns a JSON-serialisable result. */
    Object run();
}
```

```java
package com.example.designpatterns.catalog;

/** Metadata about one pattern, as returned by the catalog endpoints. */
public record PatternSummary(
        String id,
        Category category,
        String title,
        String intent,
        String docsUrl,
        String runUrl) {

    static PatternSummary of(PatternDemo demo) {
        return new PatternSummary(
                demo.id(),
                demo.category(),
                demo.title(),
                demo.intent(),
                "docs/patterns/" + demo.id() + ".md",
                "/api/patterns/" + demo.id());
    }
}
```

- [ ] **Step 4: Write `UnknownPatternException` and `PatternCatalog`**

```java
package com.example.designpatterns.catalog;

import java.util.Collection;
import java.util.List;

/** Thrown when a caller asks for a pattern id the catalog does not hold. */
public class UnknownPatternException extends RuntimeException {

    private final List<String> knownIds;

    public UnknownPatternException(String id, Collection<String> knownIds) {
        super("Unknown pattern '" + id + "'. Known ids: " + String.join(", ", knownIds));
        this.knownIds = List.copyOf(knownIds);
    }

    public List<String> knownIds() {
        return knownIds;
    }
}
```

```java
package com.example.designpatterns.catalog;

import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Registry of every {@link PatternDemo} in the application.
 *
 * <p>Spring injects the full list of implementations; no pattern package is
 * ever named here, so adding a package requires editing nothing.
 */
@Component
public class PatternCatalog {

    private final Map<String, PatternDemo> byId = new LinkedHashMap<>();

    public PatternCatalog(List<PatternDemo> demos) {
        demos.stream()
                .sorted(Comparator.comparing(PatternDemo::category).thenComparing(PatternDemo::id))
                .forEach(demo -> {
                    PatternDemo previous = byId.putIfAbsent(demo.id(), demo);
                    if (previous != null) {
                        throw new IllegalStateException(
                                "Duplicate pattern id '" + demo.id() + "' declared by "
                                        + previous.getClass().getName() + " and "
                                        + demo.getClass().getName());
                    }
                });
    }

    public PatternDemo findById(String id) {
        PatternDemo demo = byId.get(id);
        if (demo == null) {
            throw new UnknownPatternException(id, byId.keySet());
        }
        return demo;
    }

    public List<PatternSummary> summaries() {
        return byId.values().stream().map(PatternSummary::of).toList();
    }
}
```

- [ ] **Step 5: Run the test and verify it passes**

Run: `./gradlew test --tests '*PatternCatalogTest*'`
Expected: PASS (4 tests)

- [ ] **Step 6: Add the controller and exception handler**

```java
package com.example.designpatterns.catalog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/** Browsable index of every pattern in the showcase. */
@RestController
@RequestMapping("/api/patterns")
public class CatalogController {

    private final PatternCatalog catalog;

    public CatalogController(PatternCatalog catalog) {
        this.catalog = catalog;
    }

    /** All patterns, grouped by category. */
    @GetMapping
    public Map<Category, List<PatternSummary>> index() {
        return catalog.summaries().stream()
                .collect(Collectors.groupingBy(
                        PatternSummary::category, LinkedHashMap::new, Collectors.toList()));
    }

    /** One pattern's metadata plus the result of running its canned demo. */
    @GetMapping("/{id}")
    public Map<String, Object> run(@PathVariable String id) {
        PatternDemo demo = catalog.findById(id);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("pattern", PatternSummary.of(demo));
        body.put("result", demo.run());
        return body;
    }
}
```

```java
package com.example.designpatterns.catalog;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Maps showcase exceptions to RFC 7807 problem details. */
@RestControllerAdvice
public class CatalogExceptionHandler {

    @ExceptionHandler(UnknownPatternException.class)
    public ProblemDetail onUnknownPattern(UnknownPatternException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        detail.setTitle("Unknown pattern");
        detail.setProperty("knownIds", ex.knownIds());
        return detail;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail onIllegalArgument(IllegalArgumentException ex) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        detail.setTitle("Invalid input");
        return detail;
    }
}
```

- [ ] **Step 7: Run the full suite**

Run: `./gradlew test`
Expected: PASS. Note that `contextLoads` still passes even with zero `PatternDemo` beans — `List<PatternDemo>` injects as an empty list.

- [ ] **Step 8: Commit**

```bash
git add src/main/java/com/example/designpatterns/catalog src/test/java/com/example/designpatterns/catalog
git commit -m "feat: add pattern catalog spine with registry, controller, and problem details"
```

---

## Tasks 3-21: the nineteen pattern packages

Every pattern task follows the identical shape below. Read this once; each
task then supplies only its own specifics.

**Per-task shape:**
1. Write the behavioural test in `src/test/java/com/example/designpatterns/<path>/<Name>Test.java`.
2. Run it: `./gradlew test --tests '*<Name>Test*'` — expect FAIL (does not compile).
3. Implement the participants listed under **Files**, with the exact signatures under **Interfaces**.
4. Implement the `PatternDemo` component for the package.
5. Re-run the test — expect PASS.
6. Run `./gradlew test` — expect the whole suite green.
7. Commit: `git add src/main/.../<pkg> src/test/.../<pkg> && git commit -m "feat(<id>): add <Title> pattern demo"`

**Universal acceptance criteria for every pattern task:**
- The package contains exactly one `@Component implements PatternDemo` whose `id()` matches the leaf package name.
- No import of any other leaf pattern package.
- Class-level Javadoc on every public type naming its pattern role.
- The test asserts the pattern's guarantee, not just that the code executes.

---

### Task 3: Singleton — `creational/singleton`

**Files:**
- Create: `.../creational/singleton/RequestCounter.java` (classic eager-holder singleton)
- Create: `.../creational/singleton/CounterBean.java` (`@Component`, container-managed for contrast)
- Create: `.../creational/singleton/SingletonDemo.java`
- Test: `.../creational/singleton/SingletonTest.java`

**Interfaces — Produces:**
- `RequestCounter.getInstance() -> RequestCounter` (private constructor; static nested holder class)
- `RequestCounter.increment() -> long`, `RequestCounter.count() -> long`
- `CounterBean.increment() -> long`, `CounterBean.count() -> long`

**Test asserts:** `getInstance()` returns the identical reference across calls and across threads; state accumulates across callers; a second Spring context would get its own `CounterBean` (documented, asserted via `@SpringBootTest` bean identity within one context).

**Docs must cover:** why the enum singleton is safest in plain Java, and why `@Component` gives you a *per-container* singleton — not a JVM-wide one — which is the single most common misunderstanding.

---

### Task 4: Factory Method — `creational/factorymethod`

**Files:**
- Create: `NotificationChannel.java` (interface), `EmailChannel.java`, `SmsChannel.java`
- Create: `NotificationDispatcher.java` (abstract creator with the factory method), `EmailDispatcher.java`, `SmsDispatcher.java`
- Create: `FactoryMethodDemo.java`
- Test: `FactoryMethodTest.java`

**Interfaces — Produces:**
- `interface NotificationChannel { String send(String to, String message); }`
- `abstract class NotificationDispatcher { protected abstract NotificationChannel createChannel(); public String dispatch(String to, String message); }`

**Test asserts:** `dispatch` is defined once in the base class yet produces channel-specific output, proving subclasses vary the product without varying the algorithm.

---

### Task 5: Abstract Factory — `creational/abstractfactory`

**Files:**
- Create: `ReportHeader.java`, `ReportBody.java`, `ReportFooter.java` (interfaces)
- Create: `PdfHeader/Body/Footer.java`, `HtmlHeader/Body/Footer.java`
- Create: `ReportFactory.java` (interface), `PdfReportFactory.java`, `HtmlReportFactory.java`
- Create: `ReportAssembler.java`, `AbstractFactoryDemo.java`
- Test: `AbstractFactoryTest.java`

**Interfaces — Produces:**
- `interface ReportFactory { ReportHeader createHeader(); ReportBody createBody(); ReportFooter createFooter(); String format(); }`
- `ReportAssembler.assemble(ReportFactory factory, String title, List<String> rows) -> String`

**Test asserts:** every part produced by one factory belongs to the same family — assembling with `PdfReportFactory` never yields an HTML part. This "no mixed families" guarantee is what separates Abstract Factory from Factory Method.

---

### Task 6: Builder — `creational/builder`

**Files:**
- Create: `OrderRequest.java` (immutable, private constructor, static nested `Builder`)
- Create: `OrderLine.java` (record)
- Create: `BuilderDemo.java`, `BuilderController.java`
- Test: `BuilderTest.java`

**Interfaces — Produces:**
- `OrderRequest.builder() -> OrderRequest.Builder`
- `Builder.customer(String)`, `.addLine(String sku, int qty, BigDecimal unitPrice)`, `.shipTo(String)`, `.expedited(boolean)`, `.build() -> OrderRequest` (all fluent)
- `OrderRequest.total() -> BigDecimal`

**Test asserts:** `build()` rejects an order with no lines and with a blank customer (invariants enforced at construction, so no invalid `OrderRequest` can exist); the built object exposes no setters; `total()` is correct.

**Endpoint:** `POST /api/patterns/builder/orders` accepting a JSON body and returning the built order.

---

### Task 7: Prototype — `creational/prototype`

**Files:**
- Create: `ReportTemplate.java` (implements a `copy()` returning a deep copy)
- Create: `Watermark.java` (mutable nested object, to make deep-vs-shallow visible)
- Create: `TemplateRegistry.java`, `PrototypeDemo.java`
- Test: `PrototypeTest.java`

**Interfaces — Produces:**
- `ReportTemplate.copy() -> ReportTemplate`
- `TemplateRegistry.register(String name, ReportTemplate prototype) -> void`
- `TemplateRegistry.newInstance(String name) -> ReportTemplate`

**Test asserts:** mutating a copy's nested `Watermark` does **not** affect the registered prototype. This is the whole lesson — a shallow copy would fail this test, and the docs must show that failing version.

---

### Task 8: Adapter — `structural/adapter`

**Files:**
- Create: `ShippingService.java` (target interface), `ShippingQuote.java` (record)
- Create: `LegacyXmlShippingApi.java` (adaptee — returns an XML-ish `String`, cannot be changed)
- Create: `LegacyShippingAdapter.java`, `AdapterDemo.java`
- Test: `AdapterTest.java`

**Interfaces — Produces:**
- `interface ShippingService { ShippingQuote quote(String postcode, double weightKg); }`
- `LegacyXmlShippingApi.getRateXml(String zip, int grams) -> String`
- `record ShippingQuote(String carrier, BigDecimal price, int estimatedDays)`

**Test asserts:** the adapter satisfies `ShippingService` while calling only the legacy signature, and correctly converts units (kg→grams) and formats (XML→record). Callers depend on `ShippingService` and never see the legacy type.

---

### Task 9: Decorator — `structural/decorator`

**Files:**
- Create: `MessageSender.java` (component interface), `PlainMessageSender.java` (concrete component)
- Create: `MessageSenderDecorator.java` (abstract base), `LoggingSender.java`, `RetryingSender.java`, `EncryptingSender.java`
- Create: `DecoratorDemo.java`
- Test: `DecoratorTest.java`

**Interfaces — Produces:**
- `interface MessageSender { String send(String message); }`
- `RetryingSender(MessageSender delegate, int maxAttempts)`
- `EncryptingSender(MessageSender delegate)` — a trivial reversible cipher, clearly labelled as not real cryptography

**Test asserts:** wrapping order changes observable behaviour (encrypt-then-log logs ciphertext; log-then-encrypt logs plaintext); a flaky delegate succeeds on the third attempt under `RetryingSender(3)` and fails under `RetryingSender(2)`; each decorator invokes its delegate exactly once per successful call.

---

### Task 10: Proxy — `structural/proxy`

**Files:**
- Create: `ImageService.java` (subject), `RealImageService.java` (expensive; records load count)
- Create: `LazyImageProxy.java` (virtual proxy), `ProtectedImageProxy.java` (protection proxy)
- Create: `AuditedService.java` + `AuditAspect.java` (`@Aspect`, showing Spring's own proxying)
- Create: `ProxyDemo.java`
- Test: `ProxyTest.java`, `AuditAspectTest.java` (`@SpringBootTest`)

**Interfaces — Produces:**
- `interface ImageService { byte[] load(String id); }`
- `ProtectedImageProxy(ImageService delegate, Set<String> allowedIds)` — throws `IllegalArgumentException` on a disallowed id
- `AuditedService.perform(String action) -> String`, advised by `AuditAspect`

**Test asserts:** the real service is not constructed until first `load()` (virtual proxy defers cost); the protection proxy blocks disallowed ids before the delegate runs; and the Spring-injected `AuditedService` is not the raw class — `AopUtils.isAopProxy(bean)` is true, which is exactly how `@Transactional` works.

---

### Task 11: Facade — `structural/facade`

**Files:**
- Create: `InventorySubsystem.java`, `PaymentSubsystem.java`, `ShippingSubsystem.java`
- Create: `CheckoutFacade.java`, `CheckoutResult.java` (record), `FacadeDemo.java`, `FacadeController.java`
- Test: `FacadeTest.java`

**Interfaces — Produces:**
- `CheckoutFacade.checkout(String sku, int qty, String card, String address) -> CheckoutResult`
- `record CheckoutResult(boolean success, String orderId, String message, List<String> stepsTaken)`

**Test asserts:** one facade call drives all three subsystems in the right order; a failure in payment stops shipping from being invoked and reserved inventory is released. The caller never touches a subsystem type directly.

---

### Task 12: Composite — `structural/composite`

**Files:**
- Create: `OrgNode.java` (component interface), `Employee.java` (leaf), `Department.java` (composite)
- Create: `CompositeDemo.java`
- Test: `CompositeTest.java`

**Interfaces — Produces:**
- `interface OrgNode { String name(); int headcount(); BigDecimal monthlyCost(); String render(int indent); }`
- `Department.add(OrgNode child) -> Department`

**Test asserts:** `headcount()` and `monthlyCost()` on a nested department equal the sums over the whole subtree, and a client method accepting `OrgNode` works identically whether handed a leaf or a composite — the uniformity guarantee.

---

### Task 13: Strategy — `behavioral/strategy`

**Files:**
- Create: `PaymentStrategy.java`, `CreditCardStrategy.java`, `PayPalStrategy.java`, `CryptoStrategy.java`
- Create: `PaymentService.java` (holds `Map<String, PaymentStrategy>`), `PaymentResult.java` (record)
- Create: `StrategyDemo.java`, `StrategyController.java`
- Test: `StrategyTest.java`

**Interfaces — Produces:**
- `interface PaymentStrategy { String key(); PaymentResult pay(BigDecimal amount); }`
- `PaymentService.pay(String key, BigDecimal amount) -> PaymentResult`
- `record PaymentResult(String method, BigDecimal amount, BigDecimal fee, String reference)`

**Test asserts:** each strategy applies its own fee schedule; and — the key property — constructing `PaymentService` with a fourth, test-local strategy makes it immediately selectable **without editing `PaymentService` or any existing strategy**. That is the open/closed guarantee Strategy buys.

**Endpoint:** `POST /api/patterns/strategy/pay?method=paypal&amount=100.00`

> **USER DECISION REQUIRED — fallback policy.** `PaymentService.pay` must decide what happens when `key` matches no strategy: throw (fail fast, caller must handle), fall back to a default strategy (never fails, may surprise), or return a `PaymentResult` marked unsuccessful (uniform return type, callers must check). Stop here and ask the user to write the 5-10 line body of `PaymentService.pay`. Leave a clearly marked `// TODO(user)` block with the signature and a comment stating the three options.

---

### Task 14: Observer — `behavioral/observer`

**Files:**
- Create: `OrderEvent.java` (record), `OrderEventListener.java` (interface), `AuditListener.java`, `EmailListener.java`, `MetricsListener.java`
- Create: `OrderEventPublisher.java` (hand-rolled subject), `SpringOrderEvents.java` (`ApplicationEventPublisher` version), `ObserverDemo.java`
- Test: `ObserverTest.java`, `SpringObserverTest.java` (`@SpringBootTest`)

**Interfaces — Produces:**
- `record OrderEvent(String orderId, String type, Instant at)`
- `interface OrderEventListener { void onEvent(OrderEvent event); }`
- `OrderEventPublisher.subscribe(OrderEventListener) -> void`, `.unsubscribe(...)`, `.publish(OrderEvent) -> int` (returns listeners notified)

**Test asserts:** publishing reaches every subscriber; unsubscribing stops delivery; a listener that throws does not prevent the remaining listeners from being notified (isolation — a real failure mode of naive Observer). The Spring test asserts `@EventListener` methods receive the same event.

---

### Task 15: Template Method — `behavioral/templatemethod`

**Files:**
- Create: `DataImportJob.java` (abstract, `final` template method), `CsvImportJob.java`, `JsonImportJob.java`
- Create: `ImportReport.java` (record), `TemplateMethodDemo.java`
- Test: `TemplateMethodTest.java`

**Interfaces — Produces:**
- `abstract class DataImportJob { public final ImportReport run(String raw); protected abstract List<Map<String,String>> parse(String raw); protected abstract boolean validate(Map<String,String> row); protected void onSkipped(Map<String,String> row) {} }`
- `record ImportReport(String format, int parsed, int imported, int skipped, List<String> steps)`

**Test asserts:** both subclasses execute the identical step sequence (captured in `ImportReport.steps`) despite parsing different formats, and the hook `onSkipped` fires only for invalid rows. `run` being `final` is asserted reflectively — subclasses may fill in steps but may not reorder them.

---

### Task 16: Chain of Responsibility — `behavioral/chainofresponsibility`

**Files:**
- Create: `ValidationHandler.java` (interface), `NotBlankHandler.java`, `LengthHandler.java`, `ProfanityHandler.java`, `RateLimitHandler.java`
- Create: `ValidationChain.java`, `ValidationResult.java` (record), `ChainDemo.java`, `ChainController.java`
- Test: `ChainTest.java`

**Interfaces — Produces:**
- `interface ValidationHandler { int order(); String name(); Optional<String> validate(String input); }` — `Optional.empty()` means pass, a present value is the rejection reason
- `ValidationChain.validate(String input) -> ValidationResult`
- `record ValidationResult(boolean valid, List<String> failures, List<String> handlersRun)`

**Test asserts:** handlers run in `order()` sequence regardless of injection order, and the short-circuit policy chosen below is honoured — asserted via `handlersRun`.

> **USER DECISION REQUIRED — short-circuit policy.** Stop at the first failing handler (fast, but the caller fixes one problem at a time) or run every handler and collect all failures (slower, better UX for form validation, but a handler may then run on input an earlier handler already rejected). Ask the user to write the 5-10 line loop body of `ValidationChain.validate`. Leave a marked `// TODO(user)` block with both options in a comment.

---

### Task 17: Command — `behavioral/command`

**Files:**
- Create: `CartCommand.java` (interface), `AddItemCommand.java`, `RemoveItemCommand.java`, `ApplyDiscountCommand.java`
- Create: `Cart.java` (receiver), `CommandInvoker.java` (history stack), `CommandDemo.java`, `CommandController.java`
- Test: `CommandTest.java`

**Interfaces — Produces:**
- `interface CartCommand { String describe(); void execute(Cart cart); void undo(Cart cart); }`
- `CommandInvoker.run(CartCommand) -> void`, `.undoLast() -> Optional<String>`, `.history() -> List<String>`

**Test asserts:** executing three commands then undoing all three returns the cart to its exact starting state (the round-trip property), undo order is LIFO, and `undoLast()` on an empty history returns `Optional.empty()` rather than throwing.

---

### Task 18: State — `behavioral/state`

**Files:**
- Create: `OrderState.java` (sealed interface), `NewState.java`, `PaidState.java`, `ShippedState.java`, `DeliveredState.java`, `CancelledState.java`
- Create: `Order.java` (context), `TransitionResult.java` (record), `StateDemo.java`, `StateController.java`
- Test: `StateTest.java`

**Interfaces — Produces:**
- `sealed interface OrderState permits NewState, PaidState, ShippedState, DeliveredState, CancelledState { String name(); TransitionResult on(OrderEventType event); }`
- `enum OrderEventType { PAY, SHIP, DELIVER, CANCEL }`
- `Order.handle(OrderEventType) -> TransitionResult`, `Order.state() -> OrderState`
- `record TransitionResult(boolean accepted, String from, String to, String reason)`

**Test asserts:** the happy path NEW→PAID→SHIPPED→DELIVERED is accepted; every illegal transition is refused and leaves the state unchanged; terminal states accept nothing. Because `OrderState` is sealed, a `switch` over it needs no `default` — the compiler proves the handling is exhaustive, which is Java 17's improvement on classic State.

> **USER DECISION REQUIRED — transition table and refusal behaviour.** Which transitions are legal (can a SHIPPED order be cancelled? can a DELIVERED order be re-paid?), and whether an illegal transition throws `IllegalStateException` or returns `TransitionResult(accepted=false, ...)`. Ask the user to fill in the transition table — 5-10 lines. Leave a marked `// TODO(user)` block listing the 5 states × 4 events grid.

---

### Task 19: Dependency Injection / IoC — `spring/dependencyinjection`

**Files:**
- Create: `GreetingService.java` (interface), `FormalGreetingService.java` (`@Profile("!casual")`), `CasualGreetingService.java` (`@Profile("casual")`)
- Create: `ClockProvider.java` with two `@Qualifier`-distinguished beans, `GreetingFacade.java`, `DependencyInjectionDemo.java`
- Test: `DependencyInjectionTest.java` (`@SpringBootTest`), `CasualProfileTest.java` (`@ActiveProfiles("casual")`)

**Interfaces — Produces:**
- `interface GreetingService { String greet(String name); }`
- `GreetingFacade(GreetingService service, @Qualifier("fixedClock") Clock clock)`

**Test asserts:** the default context wires `FormalGreetingService`, the `casual` profile swaps the implementation with no code change, and `@Qualifier` resolves the ambiguity between two `Clock` beans that would otherwise fail context startup. Also asserts constructor injection makes the dependency `final` and the class unit-testable with plain `new` — the argument against field injection.

---

### Task 20: Repository — `spring/repository`

**Files:**
- Create: `Book.java` (`@Entity`), `BookRepository.java` (`extends JpaRepository<Book, Long>` with a derived query)
- Create: `InMemoryBookRepository.java` (hand-written, implementing the same read methods, to show what Spring generates)
- Create: `BookService.java`, `RepositoryDemo.java`, `BookController.java`
- Create: `src/main/resources/data.sql` (seed rows)
- Test: `BookRepositoryTest.java` (`@DataJpaTest`), `InMemoryBookRepositoryTest.java` (plain unit test)

**Interfaces — Produces:**
- `Book(Long id, String title, String author, int year)` with JPA annotations
- `BookRepository.findByAuthorIgnoreCase(String author) -> List<Book>`
- `BookService.byAuthor(String) -> List<Book>`, `.add(String title, String author, int year) -> Book`

**Test asserts:** the derived query returns only matching books and is case-insensitive; the hand-written and Spring Data implementations satisfy the same test contract — proving Repository is an *interface* pattern that Spring merely automates. This is the only package touching JPA.

---

### Task 21: DTO / Assembler — `spring/dto`

**Files:**
- Create: `CustomerEntity.java` (with a deliberately sensitive `passwordHash` and `internalNotes` field)
- Create: `CustomerDto.java` (record), `CustomerAssembler.java`, `CustomerController.java`, `DtoDemo.java`
- Test: `CustomerAssemblerTest.java`

**Interfaces — Produces:**
- `record CustomerDto(Long id, String displayName, String email, String memberSince)`
- `CustomerAssembler.toDto(CustomerEntity) -> CustomerDto`
- `CustomerAssembler.toDtoList(List<CustomerEntity>) -> List<CustomerDto>`

**Test asserts:** the serialised DTO contains no `passwordHash` or `internalNotes` field — asserted by serialising with Jackson and checking the JSON keys, not by reading the record definition. That is the security property the pattern exists to provide, and it is why entities must not be returned from controllers.

---

### Task 22: Full-catalog integration test

**Files:**
- Test: `src/test/java/com/example/designpatterns/catalog/CatalogIntegrationTest.java`

- [ ] **Step 1: Write the test**

```java
package com.example.designpatterns.catalog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogIntegrationTest {

    private static final List<String> EXPECTED_IDS = List.of(
            "singleton", "factorymethod", "abstractfactory", "builder", "prototype",
            "adapter", "decorator", "proxy", "facade", "composite",
            "strategy", "observer", "templatemethod", "chainofresponsibility", "command", "state",
            "dependencyinjection", "repository", "dto");

    @Autowired MockMvc mockMvc;
    @Autowired PatternCatalog catalog;

    @Test
    void catalogHoldsAllNineteenPatterns() {
        assertThat(catalog.summaries()).extracting(PatternSummary::id)
                .containsExactlyInAnyOrderElementsOf(EXPECTED_IDS);
    }

    @Test
    void everyPatternRunsThroughItsEndpoint() throws Exception {
        for (String id : EXPECTED_IDS) {
            mockMvc.perform(get("/api/patterns/{id}", id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.pattern.id").value(id))
                    .andExpect(jsonPath("$.result").exists());
        }
    }

    @Test
    void unknownPatternReturnsProblemDetailListingValidIds() throws Exception {
        mockMvc.perform(get("/api/patterns/{id}", "does-not-exist"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Unknown pattern"))
                .andExpect(jsonPath("$.knownIds").isArray());
    }

    @Test
    void everyPatternDeclaresIntentAndDocsLink() {
        assertThat(catalog.summaries()).allSatisfy(summary -> {
            assertThat(summary.intent()).isNotBlank();
            assertThat(summary.title()).isNotBlank();
            assertThat(summary.docsUrl()).isEqualTo("docs/patterns/" + summary.id() + ".md");
        });
    }
}
```

- [ ] **Step 2: Run it**

Run: `./gradlew test --tests '*CatalogIntegrationTest*'`
Expected: PASS once all 19 pattern tasks are done. Run it after each pattern task too — it will fail on the id list until the set is complete, which is the intended progress signal.

- [ ] **Step 3: Commit**

```bash
git add src/test/java/com/example/designpatterns/catalog/CatalogIntegrationTest.java
git commit -m "test: assert all nineteen patterns are registered and runnable"
```

---

### Task 23: Documentation pages

**Files:**
- Create: `docs/patterns/README.md` (index, grouped by category, linking all 19)
- Create: `docs/patterns/<id>.md` × 19
- Create: `src/main/java/com/example/designpatterns/<path>/README.md` × 19 (three-line stubs)
- Modify: `README.md` at repo root

- [ ] **Step 1: Write each `docs/patterns/<id>.md` with these seven sections, in this order**

```markdown
# <Title>

**Category:** <Creational | Structural | Behavioral | Spring idiom>
**Package:** `com.example.designpatterns.<path>`

## Intent
<The Gang of Four one-liner.>

## The problem it solves
<The concrete pain in this app's scenario, and the naive code it replaces.>

## Structure
```mermaid
classDiagram
    ...
```

## Participants
| Role | Type | Location |
|---|---|---|
| <e.g. Concrete Strategy> | `CreditCardStrategy` | `src/main/java/.../CreditCardStrategy.java:12` |

## Trade-offs — and when NOT to use this
<Honest cost: class count, indirection, debugging difficulty. At least one
situation where reaching for this pattern is the wrong call.>

## Where Spring uses this internally
<Named framework classes the reader can go and read.>

## Try it
```bash
curl -s localhost:8080/api/patterns/<id> | jq
```
```

- [ ] **Step 2: Verify every `file:line` reference in the Participants tables resolves**

Run: `grep -rhoE 'src/main/java/[^ )`]*\.java:[0-9]+' docs/patterns/ | sort -u | while IFS=: read -r f l; do [ "$(wc -l < "$f")" -ge "$l" ] || echo "STALE: $f:$l"; done`
Expected: no output.

- [ ] **Step 3: Write the package README stubs.** Each is exactly:

```markdown
# <Title> pattern

Implementation lives in this package. Full documentation:
[`docs/patterns/<id>.md`](../../../../../../../docs/patterns/<id>.md)
```

- [ ] **Step 4: Write the root `README.md`** — what this project is, `./gradlew bootRun`, `./gradlew test`, the H2 console URL, a table of all 19 patterns linking to their docs, and a short "how to study this" section recommending catalog → one category at a time.

- [ ] **Step 5: Commit**

```bash
git add docs README.md src/main/java/com/example/designpatterns
git commit -m "docs: add per-pattern documentation and study guide"
```

---

### Task 24: Published study guide artifact

- [ ] **Step 1: Load the `artifact-design` skill**, then build a single browsable HTML page rendering all 19 pattern docs — category navigation, mermaid diagrams, syntax-highlighted participants, and the `curl` snippets.
- [ ] **Step 2: Publish it** and hand the user the URL.
- [ ] **Step 3:** No commit — the artifact is hosted, not in-repo.

---

## Self-Review Notes

**Spec coverage check:** Build baseline → Task 1. Catalog spine → Task 2. All 19
patterns → Tasks 3-21 (Creational 3-7, Structural 8-12, Behavioral 13-18,
Spring 19-21). Data/persistence → Task 20. Error handling → Task 2 Step 6.
Testing strategy → per-pattern tasks plus Task 22. Documentation → Task 23.
Artifact → Task 24. Deferred decisions → flagged in Tasks 13, 16, 18. No spec
section is unimplemented.

**Type consistency check:** `PatternDemo`'s five methods are used identically in
Tasks 2, 22, and every pattern task. `PatternSummary.of` is the only construction
path. `docsUrl` is `docs/patterns/<id>.md` in `PatternSummary.of`, in the Task 2
test, and in Task 22 — consistent. The 19 ids in Task 22 match the 19 leaf
package names in the Global Constraints and in Tasks 3-21 exactly.

**Known deviation from the writing-plans skill:** pattern tasks 3-21 specify
exact files, exact signatures, and the asserted property rather than pasting
complete Java bodies. Pasting all 19 implementations would duplicate the entire
application inside the plan. The signatures plus the property under test are
sufficient to make each task independently implementable and reviewable; the
spine tasks (1, 2, 22) do carry complete code because every other task compiles
against them.
