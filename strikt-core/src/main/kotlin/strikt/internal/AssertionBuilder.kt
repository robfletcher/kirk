package strikt.internal

import strikt.api.Assertion.Builder
import strikt.api.AtomicAssertion
import strikt.api.CompoundAssertion
import strikt.api.CompoundAssertions
import strikt.api.DescribeableBuilder
import strikt.internal.AssertionStrategy.Collecting
import strikt.internal.AssertionStrategy.Negating
import strikt.internal.opentest4j.MappingFailed

internal class AssertionBuilder<T>(
  private val context: AssertionGroup<T>,
  private val strategy: AssertionStrategy
) : DescribeableBuilder<T> {

  override fun describedAs(description: String): Builder<T> {
    if (context is DescribedNode<*>) {
      context.description = description
    }
    return this
  }

  override fun describedAs(descriptor: T.() -> String): Builder<T> {
    if (context is DescribedNode<*>) {
      context.description = context.subject.descriptor()
    }
    return this
  }

  override fun and(
    assertions: Builder<T>.() -> Unit
  ): Builder<T> {
    if (context.allowChain) {
      AssertionChainedGroup(context, context.subject)
        .let { nestedContext ->
          // collect assertions from a child block
          AssertionBuilder(nestedContext, Collecting)
            .apply(assertions)
          strategy.evaluate(nestedContext)
        }
    }
    // return the original builder for chaining
    return this
  }

  override fun not(assertions: Builder<T>.() -> Unit): Builder<T> {
    AssertionBuilder(context, Negating(Collecting)).apply(assertions)
    strategy.evaluate(context)
    return this
  }

  override fun assert(
    description: String,
    expected: Any?,
    assert: AtomicAssertion.(T) -> Unit
  ): AssertionBuilder<T> {
    val chain = if (context is AssertionChain<T>) {
      context
    } else {
      AssertionChain(context, context.subject)
    }
    if (chain.allowChain) {
      val assertion = strategy
        .appendAtomic(chain, description, expected)
      assertion.assert(chain.subject)
    }
    return AssertionBuilder(chain, strategy)
  }

  override fun compose(
    description: String,
    expected: Any?,
    assertions: Builder<T>.(T) -> Unit
  ): CompoundAssertions<T> =
    if (context.allowChain) {
      val composedContext = strategy
        .appendCompound(context, description, expected)
      AssertionBuilder(composedContext, Collecting).apply {
        assertions(context.subject)
      }
      object : CompoundAssertions<T> {
        override fun then(block: CompoundAssertion.() -> Unit): Builder<T> {
          composedContext.block()
          return this@AssertionBuilder
        }
      }
    } else {
      // return a no-op implementation, this will never get invoked. If
      // `allowChain` is `false` we may have an inappropriate subject to pass to
      // `assertions` that will raise an unexpected `IllegalArgumentException`.
      object : CompoundAssertions<T> {
        override fun then(block: CompoundAssertion.() -> Unit): Builder<T> {
          return this@AssertionBuilder
        }
      }
    }

  override fun <R> get(
    description: String,
    function: (T) -> R
  ): DescribeableBuilder<R> =
    if (context.allowChain) {
      runCatching {
        function(context.subject)
      }
        .getOrElse { ex -> throw MappingFailed(description, ex) }
        .let {
          AssertionBuilder(
            AssertionSubject(context, it, description),
            strategy
          )
        }
    } else {
      // fake return a builder that will never get invoked. If `allowChain` is
      // `false` we may have an inappropriate subject to pass to `function` that
      // will raise an unexpected `IllegalArgumentException`.
      @Suppress("UNCHECKED_CAST")
      this as AssertionBuilder<R> // TODO: no, this is a lie
    }

  override fun <R> with(
    description: String,
    function: T.() -> R,
    block: Builder<R>.() -> Unit
  ): Builder<T> {
    runCatching {
      function(context.subject)
    }
      .onSuccess {
        AssertionSubject(context, it, description)
          .also { nestedContext ->
            AssertionBuilder(nestedContext, Collecting).apply(block)
            strategy.evaluate(nestedContext)
          }
      }
      .onFailure { ex -> throw MappingFailed(description, ex) }
    return this
  }

  override fun not(): Builder<T> = AssertionBuilder(
    context,
    Negating(strategy)
  )

  override fun get(): T {
    return context.subject
  }
}
