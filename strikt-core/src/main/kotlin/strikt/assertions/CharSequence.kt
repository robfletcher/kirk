package strikt.assertions

import strikt.api.Assertion.Builder
import kotlin.text.RegexOption.IGNORE_CASE

/**
 * Asserts that the subject has a [CharSequence.length] of exactly [expected].
 */
fun <T : CharSequence> Builder<T>.hasLength(expected: Int): Builder<T> =
  assert("has length %d", expected) {
    when (it.length) {
      expected -> pass()
      else -> fail(actual = it.length)
    }
  }

/**
 * Asserts that the subject is composed of all lower-case characters.
 */
fun <T : CharSequence> Builder<T>.isLowerCase(): Builder<T> =
  passesIf("is lower case") {
    it.all(Char::isLowerCase)
  }

/**
 * Asserts that the subject is composed of all upper-case characters.
 */
fun <T : CharSequence> Builder<T>.isUpperCase(): Builder<T> =
  passesIf("is upper case") {
    it.all(Char::isUpperCase)
  }

/**
 * Asserts that the subject starts with the [expected] character.
 */
fun <T : CharSequence> Builder<T>.startsWith(expected: Char): Builder<T> =
  assert("starts with %s", expected) {
    if (it.startsWith(expected)) {
      pass()
    } else {
      fail(actual = it[0])
    }
  }

/**
 * Asserts that the subject starts with the [expected] string.
 */
fun <T : CharSequence> Builder<T>.startsWith(expected: CharSequence): Builder<T> =
  assert("starts with %s", expected) {
    if (it.startsWith(expected)) {
      pass()
    } else {
      fail(actual = it.take(expected.length))
    }
  }

/**
 * Asserts that the subject ends with the [expected] string.
 */
fun <T : CharSequence> Builder<T>.endsWith(expected: CharSequence): Builder<T> =
  assert("ends with %s", expected) {
    if (it.endsWith(expected)) {
      pass()
    } else {
      fail(actual = it.takeLast(expected.length))
    }
  }

/**
 * Asserts that the subject is a full match for the [expected] regular
 * expression.
 */
fun <T : CharSequence> Builder<T>.matches(expected: Regex): Builder<T> =
  assert("matches the regular expression %s", expected) {
    if (it.matches(expected)) {
      pass()
    } else {
      fail(actual = it)
    }
  }

/**
 * Asserts that the subject is a full match for the [expected] regular
 * expression regardless of case.
 */
fun <T : CharSequence> Builder<T>.matchesIgnoringCase(expected: Regex): Builder<T> =
  assert(
    "matches the regular expression %s (ignoring case)",
    expected
  ) { subject ->
    val isMatch = Regex(expected.pattern, IGNORE_CASE).let {
      subject.matches(it)
    }
    if (isMatch) {
      pass()
    } else {
      fail(actual = subject)
    }
  }

/**
 * Asserts that the subject contains a match for the [expected] regular
 * expression.
 */
fun <T : CharSequence> Builder<T>.contains(expected: Regex): Builder<T> =
  assert("contains a match for the regular expression %s", expected) {
    if (it.contains(expected)) {
      pass()
    } else {
      fail(actual = it)
    }
  }

/**
 * Asserts that the subject contains a match for the [expected] regular
 * expression regardless of case.
 */
fun <T : CharSequence> Builder<T>.containsIgnoringCase(expected: Regex): Builder<T> =
  assert(
    "contains a match for the regular expression %s (ignoring case)",
    expected
  ) { subject ->
    val isMatch = Regex(expected.pattern, IGNORE_CASE).let {
      subject.contains(it)
    }
    if (isMatch) {
      pass()
    } else {
      fail(actual = subject)
    }
  }

/**
 * Asserts that the subject contains the [expected] substring.
 */
fun <T : CharSequence> Builder<T>.contains(expected: CharSequence): Builder<T> =
  assert("contains %s", expected) {
    if (it.contains(expected)) {
      pass()
    } else {
      fail(actual = it)
    }
  }

/**
 * Asserts that the subject contains the [expected] substring regardless of
 * case.
 */
fun <T : CharSequence> Builder<T>.containsIgnoringCase(expected: CharSequence): Builder<T> =
  assert("contains %s (ignoring case)", expected) {
    if (it.contains(expected, ignoreCase = true)) {
      pass()
    } else {
      fail(actual = it)
    }
  }

/**
 * Maps an assertion on a [CharSequence] to an assertion on its length.
 *
 * @see CharSequence.length
 */
val <T : CharSequence> Builder<T>.length: Builder<Int>
  get() = traverse(CharSequence::length)
