---
---

# Assertion styles

Two different styles of assertion -- chained and block -- are supported for different use-cases.
You can mix and match both in the same test and even nest chained assertions inside block assertions.

## Chained assertions

Chained assertions use a fluent API similar to AssertJ.
They fail fast.
That is, the first assertion that fails breaks the chain and further assertions are not evaluated.

Each assertion in the chain returns an `Assertion.Builder` object that supports further assertions.

```kotlin
{% snippet 'assertion_styles_1' %}
```

Produces the output:

```text
{% snippet 'assertion_styles_2' %}
```

Notice that the `isUpperCase()` assertion is not applied as the earlier `hasLength(1)` assertion failed.

## Block assertions

Block assertions are declared in a lambda whose receiver is an `Assertion.Builder<T>` object.
They allow multiple assertions (or assertion chains) to be evaluated against the subject.

Block assertions do _not_ fail fast.
That is, all assertions in the block are evaluated and the result of the "compound" assertion will include results for all the assertions made in the block.

```kotlin
{% snippet 'assertion_styles_3' %}
```

Produces the output:

```text
{% snippet 'assertion_styles_4' %}
```

All assertions are applied and since two fail there are two errors logged.

### Chained assertions inside block assertions

Chained assertions inside a block _will_ still fail fast but will not prevent other assertions in the block from being evaluated.

```kotlin
{% snippet 'assertion_styles_5' %}
```

Produces the output:

```text
{% snippet 'assertion_styles_6' %}
```

Note the `isA<Int>` assertion (that would have failed) was not evaluated since it was chained after `lessThan(1)` which failed.
The `greaterThan(1)` assertion _was_ evaluated since it was not part of the same chain.

## Assertions with multiple subjects

As well as `expectThat` Strikt provides a top-level `expect` method that just takes a lambda parameter.
Inside the `expect` block you use `that` to define a subject and start a chain or nested block of assertions.

All assertions inside the `expect` lambda are evaluated.

The previous examples can be combined into a single `expect` block.

```kotlin
{% snippet 'assertion_styles_7' %}
```
