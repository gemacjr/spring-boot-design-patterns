# Design Patterns Showcase — Spring Boot Teaching Application

**Date:** 2026-09-08
**Status:** Approved for planning

## Purpose

A runnable Spring Boot application that teaches 19 design patterns. Each pattern
lives in its own self-contained package with a working implementation, a REST
endpoint that exercises it, a JUnit 5 test asserting the property the pattern
buys you, and a documentation page. The application is a study aid, not a
production system; clarity outranks cleverness everywhere.

## Success Criteria

1. `./gradlew bootRun` starts the app; `GET /api/patterns` returns all 19 patterns.
2. Every pattern is runnable in isolation through its own endpoint.
3. Every pattern has a `docs/patterns/<id>.md` page covering intent, problem,
   structure, participants, trade-offs, and its Spring-native counterpart.
4. `./gradlew test` passes with at least one behavioural test per pattern.
5. A reader can study any single pattern without reading any other package.

## Non-Goals

- Production concerns: authentication, persistence beyond H2, deployment, observability.
- Exhaustive GoF coverage. Seven rarely-encountered GoF patterns (Flyweight,
  Interpreter, Memento, Mediator, Bridge, Iterator, Visitor) are deliberately
  excluded as out of scope for a Spring-focused study.
- A "before/after anti-pattern" variant of each package.

## Build Baseline

The repository currently holds a plain Java Gradle project (`id("java")`,
Gradle 9.6 wrapper, JDK 17, empty `src`). It will be converted in place.

- Spring Boot **3.5.16** (latest 3.5.x; Boot 4.x requires Java 21, we are on 17)
- Gradle Kotlin DSL, existing wrapper retained
- Java toolchain 17
- Plugins: `org.springframework.boot`, `io.spring.dependency-management`, `java`
- Dependencies: `spring-boot-starter-web`, `spring-boot-starter-data-jpa`,
  `spring-boot-starter-validation`, `spring-boot-starter-aop`,
  `com.h2database:h2` (runtime), `spring-boot-starter-test`
- Root package: `com.example.designpatterns`
- Existing JUnit BOM block is replaced by `spring-boot-starter-test`

## Architecture

### The catalog spine

The single risk in a 19-package layout is inconsistency between packages. A
shared contract prevents drift and doubles as the first lesson (it is Strategy
plus a Registry, assembled by the container):

```java
public interface PatternDemo {
    String id();          // "strategy"
    Category category();  // CREATIONAL | STRUCTURAL | BEHAVIORAL | SPRING
    String title();       // "Strategy"
    String intent();      // one-line GoF intent
    Object run();         // executes a canned demo, returns a JSON-able result
}
```

`Category` is an enum. Each pattern package contributes exactly one
`@Component` implementing `PatternDemo`.

`PatternCatalog` receives `List<PatternDemo>` by constructor injection — Spring
collects every implementation automatically — indexes them by `id`, and rejects
duplicate ids at startup. `CatalogController` exposes:

- `GET /api/patterns` → all patterns grouped by category (id, title, intent, docs URL, run URL)
- `GET /api/patterns/{id}` → one pattern's metadata plus the result of `run()`
- Unknown id → 404 with a problem detail listing valid ids

Patterns that need caller input additionally expose their own controller under
`/api/patterns/{id}/...` (e.g. `POST /api/patterns/strategy/pay`).

### Package layout

```
com.example.designpatterns
├── DesignPatternsApplication.java
├── catalog/
│     Category.java, PatternDemo.java, PatternCatalog.java, CatalogController.java
├── creational/
│     singleton/  factorymethod/  abstractfactory/  builder/  prototype/
├── structural/
│     adapter/  decorator/  proxy/  facade/  composite/
├── behavioral/
│     strategy/  observer/  templatemethod/  chainofresponsibility/  command/  state/
└── spring/
      dependencyinjection/  repository/  dto/
```

Each leaf package contains its participants, its `PatternDemo` component, an
optional controller, and a `README.md` stub linking to the canonical doc.

Test tree mirrors the main tree exactly.

## The 19 Patterns

Each row is one leaf package. "Spring counterpart" is documented in prose and
where practical demonstrated in code.

### Creational

| id | Scenario | Spring counterpart |
|---|---|---|
| `singleton` | App-wide request counter; eager holder idiom vs. container-managed bean | Bean scopes; why a `@Component` is a per-container singleton, not a JVM singleton |
| `factorymethod` | `NotificationChannel` created by subclass-selected factory method | `@Bean` factory methods |
| `abstractfactory` | Report family (`PdfReportFactory` / `HtmlReportFactory`) producing matched header+body+footer | `FactoryBean<T>` |
| `builder` | Immutable `OrderRequest` with validation at `build()` | Records; Lombok `@Builder`; `UriComponentsBuilder` |
| `prototype` | Cloning a configured `ReportTemplate` (deep vs. shallow copy shown explicitly) | `@Scope("prototype")` |

### Structural

| id | Scenario | Spring counterpart |
|---|---|---|
| `adapter` | Legacy XML-shaped shipping API adapted to a modern `ShippingService` interface | `HandlerMethodArgumentResolver`, `HttpMessageConverter` |
| `decorator` | `MessageSender` wrapped with logging, retry, and encryption decorators | `BeanPostProcessor` |
| `proxy` | Virtual proxy (lazy image load) + protection proxy (access check) | Spring AOP, `@Transactional`, `@Cacheable` |
| `facade` | `CheckoutFacade` over inventory, payment, and shipping subsystems | The `@Service` layer as a facade over repositories |
| `composite` | Org chart: `Employee` leaf and `Department` composite, uniform `headcount()`/`cost()` | — |

### Behavioral

| id | Scenario | Spring counterpart |
|---|---|---|
| `strategy` | `PaymentStrategy` (credit card, PayPal, crypto) selected at runtime | `Map<String, PaymentStrategy>` injection; `@Qualifier` |
| `observer` | Order lifecycle events fanned out to audit, email, and metrics listeners | `ApplicationEventPublisher`, `@EventListener`, `@TransactionalEventListener` |
| `templatemethod` | `DataImportJob` fixing the pipeline, subclasses filling in parse/validate steps | `JdbcTemplate`, `RestTemplate`, `AbstractApplicationContext.refresh()` |
| `chainofresponsibility` | Ordered request-validation chain with explicit short-circuit policy | `HandlerInterceptor`, servlet `Filter`, Spring Security filter chain |
| `command` | Shopping-cart operations as undoable command objects with a history stack | `ApplicationRunner`; the Command pattern in `@Async` task submission |
| `state` | Order lifecycle state machine (NEW → PAID → SHIPPED → DELIVERED / CANCELLED) using a sealed interface | — |

### Spring idioms

| id | Scenario | Notes |
|---|---|---|
| `dependencyinjection` | Constructor injection, `@Qualifier` disambiguation, `@Profile`-selected implementation | Explains why field injection is discouraged |
| `repository` | H2 + Spring Data JPA `Book` aggregate; hand-written repository interface contrasted with `JpaRepository` | The only package touching the database |
| `dto` | Entity ↔ record DTO at the web boundary, with an assembler | Explains why entities must not leak to controllers |

## Data & Persistence

H2 in-memory, `ddl-auto: create-drop`, seeded by a `data.sql` or an
`ApplicationRunner` confined to the `repository` package. No other package
touches JPA. H2 console enabled at `/h2-console` for study.

## Error Handling

- A single `@RestControllerAdvice` in `catalog/` maps domain exceptions to RFC 7807
  `ProblemDetail` responses.
- Unknown pattern id → 404 listing valid ids.
- Invalid pattern input (e.g. unsupported payment method) → 400 with the reason.
- Deliberate demo failures (retry decorator, chain rejection) return structured
  results rather than throwing, so the teaching output stays readable.

## Testing Strategy

- One plain JUnit 5 unit test class per pattern, asserting the *property the
  pattern provides*, not merely that the code runs. Examples:
  - Strategy: a newly added strategy is picked up without editing any existing class.
  - Decorator: wrapping order is preserved and each decorator is invoked exactly once.
  - Composite: `cost()` on a department equals the sum over its subtree.
  - Chain of Responsibility: a rejecting handler stops downstream handlers from running.
  - State: illegal transitions are refused and legal ones are permitted.
- One `@SpringBootTest` + `MockMvc` smoke test asserting `/api/patterns` returns
  all 19 ids and that each `GET /api/patterns/{id}` responds 200.
- Slice test `@DataJpaTest` for the `repository` package only.

## Documentation

Canonical copy, one per pattern, at `docs/patterns/<id>.md`:

1. **Intent** — the GoF one-liner
2. **The problem it solves** — the concrete pain in this app's scenario
3. **Structure** — a mermaid class diagram
4. **Participants** — each role mapped to `file:line`
5. **Trade-offs / when NOT to use it** — the cost, honestly stated
6. **Where Spring uses this internally** — framework class names a reader can go read
7. **Try it** — a copy-pasteable `curl`

Supporting files:
- `docs/patterns/README.md` — index of all 19, grouped by category
- Each leaf package's `README.md` — three-line stub linking to the canonical doc
  (a stub, not a copy, so the two cannot drift)
- Class-level Javadoc naming the pattern role and linking to the doc page
- `README.md` at repo root — how to run, how to study, catalog table

A published Artifact web page renders the same 19 pages as a browsable study
guide once the docs exist.

## Build Order

1. Gradle conversion to Spring Boot + application skeleton + smoke test
2. `catalog/` spine (contract, registry, controller, advice) + its test
3. Creational patterns (5)
4. Structural patterns (5)
5. Behavioral patterns (6)
6. Spring idiom patterns (3)
7. Documentation pages + root README
8. Published Artifact study guide

Each pattern is independently completable, so steps 3-6 parallelise cleanly and
a failure in one package never blocks another.

## Open Points Deferred to Implementation

Three decisions carry real design trade-offs and will be brought to the user
during implementation rather than settled here:

1. **Chain of Responsibility short-circuit policy** — stop at first rejection vs.
   collect all failures.
2. **State machine legal transitions** — which transitions are permitted, and
   whether refusal throws or returns a result.
3. **Strategy fallback** — behaviour when no strategy matches the requested key.
