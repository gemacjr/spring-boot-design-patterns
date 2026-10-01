# Design Patterns in Spring Boot

A runnable teaching application: **19 design patterns**, each in its own isolated
package with a working implementation, a REST endpoint, a behavioural test, and a
detailed write-up.

The aim is not a reference card. Each pattern is applied to a concrete problem, each
write-up says honestly what the pattern *costs* and when not to use it, and each test
asserts the guarantee the pattern actually buys — not merely that the code runs.

## Run it

```bash
./gradlew bootRun
curl -s localhost:8080/api/patterns | jq          # the full catalogue
curl -s localhost:8080/api/patterns/strategy | jq # one pattern, executed
```

```bash
./gradlew test                                    # 100+ tests
```

H2 console (Repository pattern data): <http://localhost:8080/h2-console> —
JDBC URL `jdbc:h2:mem:patterns`, user `sa`, no password.

**Requirements:** JDK 17. Spring Boot 3.5.16, Gradle wrapper included.

## The catalogue

Full write-ups: **[docs/patterns/README.md](docs/patterns/README.md)**

| | Creational | Structural | Behavioral | Spring idioms |
|---|---|---|---|---|
| | [Singleton](docs/patterns/singleton.md) | [Adapter](docs/patterns/adapter.md) | [Strategy](docs/patterns/strategy.md) | [DI / IoC](docs/patterns/dependencyinjection.md) |
| | [Factory Method](docs/patterns/factorymethod.md) | [Decorator](docs/patterns/decorator.md) | [Observer](docs/patterns/observer.md) | [Repository](docs/patterns/repository.md) |
| | [Abstract Factory](docs/patterns/abstractfactory.md) | [Proxy](docs/patterns/proxy.md) | [Template Method](docs/patterns/templatemethod.md) | [DTO / Assembler](docs/patterns/dto.md) |
| | [Builder](docs/patterns/builder.md) | [Facade](docs/patterns/facade.md) | [Chain of Responsibility](docs/patterns/chainofresponsibility.md) | |
| | [Prototype](docs/patterns/prototype.md) | [Composite](docs/patterns/composite.md) | [Command](docs/patterns/command.md) | |
| | | | [State](docs/patterns/state.md) | |

## Layout

```
src/main/java/com/example/designpatterns/
├── catalog/        PatternDemo contract + registry + REST index
├── creational/     singleton · factorymethod · abstractfactory · builder · prototype
├── structural/     adapter · decorator · proxy · facade · composite
├── behavioral/     strategy · observer · templatemethod
│                   chainofresponsibility · command · state
└── spring/         dependencyinjection · repository · dto

docs/patterns/      one write-up per pattern (the source of truth)
```

Each leaf package holds its participants, one `PatternDemo` component, sometimes a
controller, and a `README.md` linking to its write-up. The test tree mirrors the main
tree exactly.

## The one rule that keeps it readable

**A pattern package may import the catalog contract and the JDK — never another
pattern package.** That is what lets you read any one of them in isolation, and it is
enforced by a test (`CatalogIntegrationTest.noPatternPackageImportsAnotherPatternPackage`),
not by good intentions.

## How the catalogue assembles itself

```java
public interface PatternDemo {
    String id(); Category category(); String title(); String intent(); Object run();
}

@Component
public class PatternCatalog {
    public PatternCatalog(List<PatternDemo> demos) { ... }   // Spring supplies all of them
}
```

Spring collects every implementation, so adding a pattern means adding a package and
editing nothing. The scaffolding is itself a Strategy plus a Registry — lesson zero.

## Design decisions worth knowing

Three behaviours were deliberate choices rather than defaults, and each write-up
explains what was rejected and why:

| Where | Decision |
|---|---|
| [Strategy](docs/patterns/strategy.md) | An unknown payment method **throws** rather than falling back to a default — a typo must not silently charge the wrong rail |
| [Chain of Responsibility](docs/patterns/chainofresponsibility.md) | The chain **collects every failure** rather than stopping at the first, so a user fixes a form in one pass |
| [State](docs/patterns/state.md) | An illegal transition **returns a refusal** rather than throwing — "you cannot cancel a shipped order" is a business answer, not an exception |

## Documentation

Every pattern's write-up covers: Intent · The problem it solves · Structure (mermaid
diagram) · Participants (mapped to files) · **Trade-offs and when NOT to use it** ·
Where Spring uses it internally · A `curl` to run it.
