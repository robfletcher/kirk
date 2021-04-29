package strikt.api

import filepeek.LambdaBody
import strikt.internal.FilePeek
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KFunction
import kotlin.reflect.KProperty

/**
 * Allows assertion implementations to determine a result.
 */
interface Assertion {
  /**
   * Mark this result as passed.
   *
   * @param description An optional description of the assertion result.
   */
  fun pass(description: String? = null)

  /**
   * Mark this result as failed.
   *
   * @param description An optional description of the failure.
   * @property cause The exception that caused the failure, if any.
   */
  fun fail(
    description: String? = null,
    cause: Throwable? = null
  )

  /**
   * Used to construct assertions.
   *
   * @param T the subject type.
   *
   * @see expectThat
   * @see Assertion
   */
  interface Builder<T> {

    /**
     * Evaluates a condition that may pass or fail.
     *
     * While this method _can_ be used directly in a test but is typically used
     * inside an extension method on `Assertion.Builder<T>` such as those
     * provided in the [strikt.assertions] package.
     *
     * @param description a description for the condition the assertion
     * evaluates.
     * @param assert the assertion implementation that should result in a call
     * to [Assertion.pass] or [Assertion.fail].
     * @return this assertion builder, in order to facilitate a fluent API.
     * @see Assertion.pass
     * @see Assertion.fail
     */
    fun assert(
      description: String,
      assert: AtomicAssertion.(T) -> Unit
    ): Builder<T> =
      assert(description, null, assert)

    /**
     * Evaluates a condition that may pass or fail.
     *
     * While this method _can_ be used directly in a test but is typically used
     * inside an extension method on `Assertion.Builder<T>` such as those
     * provided in the [strikt.assertions] package.
     *
     * @param description a description for the condition the assertion
     * evaluates. The description may contain a [String.format] style
     * placeholder for the [expected] value.
     * @param expected the expected value of a comparison.
     * @param assert the assertion implementation that should result in a call
     * to [Assertion.pass] or [Assertion.fail].
     * @return this assertion builder, in order to facilitate a fluent API.
     * @see Assertion.pass
     * @see Assertion.fail
     */
    fun assert(
      description: String,
      expected: Any?,
      assert: AtomicAssertion.(T) -> Unit
    ): Builder<T>

    /**
     * Allows an assertion to be composed of multiple sub-assertions such as on
     * fields of an object or elements of a collection.
     *
     * The results of assertions made inside the [assertions] block are included
     * under the overall assertion result.
     *
     * @param description a description for the condition the assertion
     * evaluates.
     * @param expected the expected value of a comparison.
     * @param assertions a group of assertions that will be evaluated against
     * the subject.
     * @return the results of assertions made inside the [assertions] block used
     * to assertAll whether the overall assertion passes or fails.
     */
    fun compose(
      description: String,
      expected: Any?,
      assertions: Builder<T>.(T) -> Unit
    ): CompoundAssertions<T>

    /**
     * Allows an assertion to be composed of multiple sub-assertions such as on
     * fields of an object or elements of a collection.
     *
     * The results of assertions made inside the [assertions] block are included
     * under the overall assertion result.
     *
     * @param description a description for the condition the assertion
     * evaluates.
     * @param assertions a group of assertions that will be evaluated against
     * the subject.
     * @return the results of assertions made inside the [assertions] block used
     * to assertAll whether the overall assertion passes or fails.
     */
    fun compose(
      description: String,
      assertions: Builder<T>.(T) -> Unit
    ): CompoundAssertions<T> =
      compose(description, null, assertions)

    /**
     * Evaluates a boolean condition.
     * This is useful for implementing the simplest types of assertion function.
     *
     * @param description a description for the condition the assertion
     * evaluates.
     * @param assert a function that returns `true` (the assertion passes) or
     * `false` (the assertion fails).
     * @return this assertion builder, in order to facilitate a fluent API.
     */
    @Deprecated(
      "Use assertThat instead",
      replaceWith = ReplaceWith("assertThat(description, assert)")
    )
    fun passesIf(description: String, assert: (T) -> Boolean): Builder<T> =
      assertThat(description, assert)

    /**
     * Evaluates a boolean condition.
     * This is useful for implementing the simplest types of assertion function.
     *
     * @param description a description for the condition the assertion
     * evaluates.
     * @param assert a function that returns `true` (the assertion passes) or
     * `false` (the assertion fails).
     * @return the chained assertion builder, in order to facilitate a fluent API.
     */
    fun assertThat(description: String, assert: (T) -> Boolean): Builder<T> =
      assert(description) {
        if (assert(it)) pass() else fail()
      }

    /**
     * Evaluates a boolean condition.
     * This is useful for implementing the simplest types of assertion function.
     *
     * @param description a description for the condition the assertion
     * evaluates.
     * @param expected the expected value of a comparison.
     * @param assert a function that returns `true` (the assertion passes) or
     * `false` (the assertion fails).
     * @return this assertion builder, in order to facilitate a fluent API.
     */
    @Deprecated(
      "Use assertThat instead",
      replaceWith = ReplaceWith("assertThat(description, expected, assert)")
    )
    fun passesIf(
      description: String,
      expected: Any?,
      assert: (T) -> Boolean
    ): Builder<T> =
      assertThat(description, expected, assert)

    /**
     * Evaluates a boolean condition.
     * This is useful for implementing the simplest types of assertion function.
     *
     * @param description a description for the condition the assertion
     * evaluates.
     * @param expected the expected value of a comparison.
     * @param assert a function that returns `true` (the assertion passes) or
     * `false` (the assertion fails).
     * @return the chained assertion builder, in order to facilitate a fluent API.
     */
    fun assertThat(
      description: String,
      expected: Any?,
      assert: (T) -> Boolean
    ): Builder<T> =
      assert(description, expected) {
        if (assert(it)) pass() else fail()
      }

    /**
     * Maps the assertion subject to the result of [function].
     * This is useful for chaining to property values or method call results on
     * the subject.
     *
     * If [function] is a callable reference, (for example a getter or property
     * reference) the subject description will be automatically determined for
     * the returned assertion builder.
     *
     * If [function] is a lambda, Strikt will make a best-effort attempt to
     * determine an appropriate function / property name.
     *
     * @param function a lambda whose receiver is the current assertion subject.
     * @return an assertion builder whose subject is the value returned by
     * [function].
     */
    fun <R> get(function: T.() -> R): DescribeableBuilder<R> =
      get(function.describe(), function)

    /**
     * Runs a group of assertions on the subject returned by [function].
     *
     * @param description a description of the mapped result.
     * @param function a lambda whose receiver is the current assertion subject.
     * @param block a closure that can perform multiple assertions that will all
     * be evaluated regardless of whether preceding ones pass or fail.
     * @param R the mapped subject type.
     * @return this builder, to facilitate chaining.
     */
    fun <R> with(
      description: String,
      function: T.() -> R,
      block: Builder<R>.() -> Unit
    ): Builder<T>

    /**
     * Runs a group of assertions on the subject returned by [function].
     *
     * @param function a lambda whose receiver is the current assertion subject.
     * @param block a closure that can perform multiple assertions that will all
     * be evaluated regardless of whether preceding ones pass or fail.
     * @param R the mapped subject type.
     * @return this builder, to facilitate chaining.
     */
    fun <R> with(
      function: T.() -> R,
      block: Builder<R>.() -> Unit
    ) = with(function.describe(), function, block)

    /**
     * Maps the assertion subject to the result of [function].
     * This is useful for chaining to property values or method call results on
     * the subject.
     *
     * @param description a description of the mapped result.
     * @param function a lambda whose receiver is the current assertion subject.
     * @return an assertion builder whose subject is the value returned by
     * [function].
     */
    fun <R> get(
      description: String,
      function: T.() -> R
    ): DescribeableBuilder<R>

    /**
     * Deprecated form of [with]`((T) -> R)`.
     *
     * @see with((T) -> R)
     */
    @Deprecated(
      "Use get instead",
      replaceWith = ReplaceWith("get(function)")
    )
    fun <R> chain(function: (T) -> R): DescribeableBuilder<R> =
      get(function)

    /**
     * Deprecated form of [with]`(String, (T) -> R)`.
     *
     * @see with(String (T) -> R)
     */
    @Deprecated(
      "Use get instead",
      replaceWith = ReplaceWith("get(description, function)")
    )
    fun <R> chain(
      description: String,
      function: (T) -> R
    ): DescribeableBuilder<R> = get(description, function)

    /**
     * Reverses any assertions chained after this method.
     *
     * @return an assertion builder that negates the results of any assertions
     * applied to its subject.
     */
    fun not(): Builder<T>

    /**
     * Evaluates a block of assertions on the current subject by executing them in reverse.
     *
     * @param assertions the assertions to evaluate in reverse
     * @see not
     */
    infix fun not(assertions: Builder<T>.() -> Unit): Builder<T>

    /**
     * Evaluates a block of assertions on the current subject.
     *
     * The main use for this method is after [strikt.assertions.isNotNull] or
     * [strikt.assertions.isA] in order that a group of assertions can more
     * conveniently be performed on the narrowed subject type.
     *
     * This method may be used as an infix function which tends to enhance
     * readability when it directly follows a lambda.
     */
    infix fun and(
      assertions: Builder<T>.() -> Unit
    ): Builder<T>

    /**
     * Return the subject. This can be useful to split up big expectThat blocks into smaller assertions
     */
    fun get(): T
  }
}

private fun <Receiver, Result> (Receiver.() -> Result).describe(): String =
  when (this) {
    is KProperty<*> ->
      "value of property $name"
    is KFunction<*> ->
      "return value of $name"
    is CallableReference -> "value of $propertyName"
    else -> {
      try {
        val line = FilePeek.filePeek.getCallerFileInfo().line
        LambdaBody("get", line).body.trim()
      } catch (e: Exception) {
        "%s"
      }
    }
  }

private val CallableReference.propertyName: String
  get() = "^get(.+)$".toRegex().find(name).let { match ->
    return when (match) {
      null -> name
      else -> match.groupValues[1].decapitalize()
    }
  }
