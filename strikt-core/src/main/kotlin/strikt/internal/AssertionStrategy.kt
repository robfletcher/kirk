package strikt.internal

import strikt.api.Status
import strikt.api.Status.Failed
import strikt.api.Status.Passed
import strikt.api.Status.Pending
import strikt.internal.opentest4j.AtomicAssertionFailure
import strikt.internal.reporting.writeToString

internal sealed class AssertionStrategy {

  fun <T> appendAtomic(
    context: AssertionGroup<T>,
    description: String,
    expected: Any?
  ): AtomicAssertionNode<T> =
    object : AtomicAssertionNode<T>(
      context,
      provideDescription(description),
      expected
    ) {

      private var _status: Status = Pending

      override val status: Status
        get() = _status

      override fun pass() {
        _status = onPass()
        afterStatusSet(this)
      }

      override fun fail(description: String?, cause: Throwable?) {
        _status = onFail(description = description, cause = cause)
        afterStatusSet(this)
      }

      override fun fail(actual: Any?, description: String?, cause: Throwable?) {
        _status = onFail(
          description = description,
          comparison = ComparedValues(expected, actual),
          cause = cause
        )
        afterStatusSet(this)
      }
    }

  fun <T> appendCompound(
    context: AssertionGroup<T>,
    description: String,
    expected: Any?
  ): CompoundAssertionNode<T> =
    object : CompoundAssertionNode<T>(
      context,
      provideDescription(description),
      expected
    ) {

      private var _status: Status = Pending

      override val status: Status
        get() = _status

      override fun pass() {
        _status = onPass()
        afterStatusSet(this)
      }

      override fun fail(description: String?, cause: Throwable?) {
        _status = onFail(description = description, cause = cause)
        afterStatusSet(this)
      }

      override val anyFailed: Boolean
        get() = children.any { it.status is Failed }
      override val allFailed: Boolean
        get() = children.all { it.status is Failed }
      override val anyPassed: Boolean
        get() = children.any { it.status is Passed }
      override val allPassed: Boolean
        get() = children.all { it.status is Passed }
    }

  protected open fun provideDescription(default: String) = default

  protected open fun <T> afterStatusSet(result: AssertionResult<T>) {}

  protected open fun onPass(): Status = Passed

  protected open fun onFail(
    description: String? = null,
    comparison: ComparedValues? = null,
    cause: Throwable? = null
  ): Status = Failed(description, comparison, cause)

  object Collecting : AssertionStrategy()

  object Throwing : AssertionStrategy() {
    override fun <T> afterStatusSet(result: AssertionResult<T>) {
      if (result.status is Failed) {
        throw AtomicAssertionFailure(result.root.writeToString(), result)
      }
    }
  }

  class Negating(
    private val delegate: AssertionStrategy
  ) : AssertionStrategy() {
    override fun provideDescription(default: String) =
      listOf(
        Regex("^is not\\b") to "is",
        Regex("^is\\b") to "is not",
        Regex("^contains\\b") to "does not contain",
        Regex("^starts with\\b") to "does not start with",
        Regex("^ends with\\b") to "does not end with",
        Regex("^matches\\b") to "does not match",
        Regex("^throws\\b") to "does not throw",
        Regex("^has\\b") to "does not have"
      ).find { (regex, _) ->
        regex.containsMatchIn(default)
      }?.let { (regex, replacement) ->
        default.replace(regex, replacement)
      } ?: "does not match: $default"

    override fun onPass() = Failed()

    override fun onFail(
      description: String?,
      comparison: ComparedValues?,
      cause: Throwable?
    ) = Passed

    override fun <T> afterStatusSet(result: AssertionResult<T>) {
      delegate.afterStatusSet(result)
    }
  }
}
