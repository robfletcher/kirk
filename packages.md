# Module strikt-core

The core API of Strikt.

## Getting started

Import Strikt's API and standard assertions library into your test.

```kotlin
import strikt.api.*
import strikt.assertions.*
```

Create assertions with `expect` or `expectThat` and apply assertion functions to create tests.

```kotlin
val subject = "fnord"
expectThat(subject).matches("[cefov]+")
```

# Package strikt.api

Contains the API classes of Strikt as well as the `expect`, `expectThat`, and `expectThrows` functions used to create assertions.

## Chained assertions

Assertions chained after `expectThat` are _fail-fast_.
That is, the first assertion that fails breaks the chain resulting in any following assertions not being evaluated.

```kotlin
expectThat(person.name)
  .isA<String>()
  .matches("[A-Z][a-z]+")
  .map { length }
  .isGreaterThan(2)
```

## Block assertions

Assertions contained in a lambda function passed to `expectThat` do _not_ fail fast.
They are all evaluated and only then are failures reported.

```kotlin
expectThat(person) {
  isNotNull()
  map { name }.matches("[A-Z][a-z]+")
  map { birthDate }.isGreaterThan(LocalDate.of(1970, 1, 1))
}
```

Of course, you can include fail-fast chains _inside_ blocks. 

# Package strikt.assertions

Contains Strikt's standard library of assertion functions.
All assertion functions are extensions on `strikt.api.Assertion<T>`.
