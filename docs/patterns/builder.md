# Builder

**Category:** Creational
**Package:** `com.example.designpatterns.creational.builder`

## Intent

Separate the construction of a complex object from its representation, so the same
construction process can create different representations.

In everyday Java the motivation is narrower and sharper: **make invalid objects
impossible to construct, and make the call site readable.**

## The problem it solves

The alternative is a telescoping constructor:

```java
new OrderRequest("Ada Lovelace", lines, "12 Analytical Way", true, false, null);
//                                                            ^^^^  ^^^^^  ^^^^
//                                          which flag is which? what is null?
```

Adding a field breaks every caller. Optional fields multiply the constructor
overloads. And validation that spans fields ("an order needs a customer *and* at
least one line") has nowhere to live, because a constructor only sees what it was
given.

The builder fixes all three:

```java
OrderRequest.builder()
    .customer("Ada Lovelace")
    .addLine("SKU-1", 2, new BigDecimal("10.00"))
    .shipTo("12 Analytical Way")
    .expedited(true)
    .build();
```

Note **where** each check happens. `addLine` rejects a non-positive quantity
immediately, at the call that supplied it — the stack trace points at the culprit.
`build()` performs the whole-object checks, because only there is the object
complete.

The payoff is not convenience, it is a system-wide invariant: **no `OrderRequest`
anywhere is ever in an invalid state**, so no downstream code needs a defensive
check.

## Structure

```mermaid
classDiagram
    class OrderRequest {
        -OrderRequest(Builder)
        +builder()$ Builder
        +customer() String
        +lines() List~OrderLine~
        +total() BigDecimal
    }
    class Builder {
        +customer(String) Builder
        +addLine(sku, qty, price) Builder
        +shipTo(String) Builder
        +expedited(boolean) Builder
        +build() OrderRequest
    }
    OrderRequest +-- Builder : static nested
    Builder ..> OrderRequest : creates via private ctor
    OrderRequest o-- OrderLine
    note for Builder "Mutable while assembling.\nDiscarded after build()."
```

## Participants

| Role | Type | Location |
|---|---|---|
| Product | `OrderRequest` | `src/main/java/com/example/designpatterns/creational/builder/OrderRequest.java` |
| Builder | `OrderRequest.Builder` | same file, static nested |
| Part | `OrderLine` (record) | `src/main/java/com/example/designpatterns/creational/builder/OrderLine.java` |
| Client | `BuilderController` | `src/main/java/com/example/designpatterns/creational/builder/BuilderController.java` |

## Trade-offs — and when NOT to use it

- **Duplication.** Every field appears in the product and again in the builder.
- **Failures move to runtime.** A missing required field is an `IllegalStateException`
  at `build()`, not a compile error. A required-args constructor would have caught it
  at compile time.
- **More code for small objects.** Below four or five fields it rarely pays.

**Don't use it** when a `record` will do. If every field is required and there is no
cross-field validation, `record OrderRequest(...)` with a compact constructor is
shorter and gives you compile-time enforcement of arity. Reach for a builder when
optional fields, cross-field invariants, or an ugly call site actually exist.

## Where Spring uses this internally

`UriComponentsBuilder`, `MockMvcRequestBuilders`, `BeanDefinitionBuilder`,
`WebClient.builder()`, and Spring Security's `HttpSecurity` DSL are all builders.
Lombok's `@Builder` generates this exact shape — worth knowing by hand so you can
tell when the generated version is not enough (it will not run your `build()`
validation unless you write it).

## Try it

```bash
curl -s localhost:8080/api/patterns/builder | jq

curl -s -X POST localhost:8080/api/patterns/builder/orders \
  -H 'Content-Type: application/json' \
  -d '{"customer":"Ada","shipTo":"London","expedited":true,
       "lines":[{"sku":"W-1","quantity":2,"unitPrice":10.00}]}' | jq

# now omit the lines and watch build() refuse
curl -s -X POST localhost:8080/api/patterns/builder/orders \
  -H 'Content-Type: application/json' \
  -d '{"customer":"Ada","shipTo":"London","lines":[]}'
```
