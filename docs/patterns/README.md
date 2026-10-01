# Design Pattern Catalogue

Nineteen patterns, each in its own self-contained package with a working
implementation, a REST endpoint, a behavioural test, and a write-up.

Each write-up follows the same seven sections: **Intent**, **The problem it solves**,
**Structure** (diagram), **Participants**, **Trade-offs — and when NOT to use it**,
**Where Spring uses this internally**, and **Try it**.

## Creational — how objects get made

| Pattern | One line | Scenario here |
|---|---|---|
| [Singleton](singleton.md) | One instance, globally reachable | Request counter, hand-rolled vs. container-managed |
| [Factory Method](factorymethod.md) | Subclasses choose the product | Notification channels |
| [Abstract Factory](abstractfactory.md) | Create a matched *family* of products | PDF vs. HTML report parts |
| [Builder](builder.md) | Assemble step by step; reject invalid objects at `build()` | Immutable `OrderRequest` |
| [Prototype](prototype.md) | Create by copying a configured instance | Cloning report templates (deep vs. shallow) |

## Structural — how objects are put together

| Pattern | One line | Scenario here |
|---|---|---|
| [Adapter](adapter.md) | Make an incompatible interface fit | Legacy XML shipping API |
| [Decorator](decorator.md) | Add behaviour by wrapping, composably | Logging / retry / encryption on a sender |
| [Proxy](proxy.md) | Stand in for an object to control access | Lazy loading, access checks, and Spring AOP |
| [Facade](facade.md) | One simple entry point over a subsystem | Checkout over inventory + payment + shipping |
| [Composite](composite.md) | Treat a tree and a leaf identically | Org chart headcount and cost |

## Behavioral — how objects collaborate

| Pattern | One line | Scenario here |
|---|---|---|
| [Strategy](strategy.md) | Interchangeable algorithms, chosen at runtime | Payment rails and their fees |
| [Observer](observer.md) | Announce; whoever cares reacts | Order lifecycle events |
| [Template Method](templatemethod.md) | Fix the algorithm, vary the steps | CSV vs. JSON import pipeline |
| [Chain of Responsibility](chainofresponsibility.md) | Pass a request along handlers | Comment validation chain |
| [Command](command.md) | Turn an operation into an object | Undoable shopping-cart operations |
| [State](state.md) | Behaviour changes with state | Order lifecycle machine |

## Spring idioms — not GoF, but everywhere in Spring code

| Pattern | One line | Scenario here |
|---|---|---|
| [Dependency Injection / IoC](dependencyinjection.md) | Declare what you need; the container supplies it | Profiles, `@Qualifier`, constructor injection |
| [Repository](repository.md) | Collection-like interface over persistence | `Book` in H2, plus a hand-written equivalent |
| [DTO / Assembler](dto.md) | Own the shape of your API boundary | Keeping `passwordHash` out of JSON |

## How to study this

1. **Start with the catalogue.** `curl -s localhost:8080/api/patterns | jq` — every
   pattern, its intent, and where to run it.
2. **Take one category at a time.** Creational is the gentlest start; Behavioral is
   where the real value is.
3. **For each pattern:** read the doc's *Intent* and *The problem it solves*, then
   open the test. The test states the guarantee the pattern buys, which is the thing
   worth remembering.
4. **Read the trade-offs section.** Knowing when *not* to reach for a pattern is
   what separates using patterns from cargo-culting them.
5. **Run it** and change something. Break the deep copy in `ReportTemplate.copy()`,
   or delete the `final` on `DataImportJob.run()`, and watch which test fails.

## How this project is wired

Every package contributes one `@Component` implementing `PatternDemo`. Spring injects
the whole `List<PatternDemo>` into `PatternCatalog`, so the index endpoint is built
from whatever exists — adding a twentieth pattern requires editing no existing file.
The scaffolding is itself a Strategy plus a Registry.

`CatalogIntegrationTest.noPatternPackageImportsAnotherPatternPackage` enforces the
isolation rule: a pattern package may import the catalog contract and the JDK, never
another pattern package. That is what makes each one readable on its own.
