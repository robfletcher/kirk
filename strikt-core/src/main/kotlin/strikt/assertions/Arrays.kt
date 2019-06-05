package strikt.assertions

import strikt.api.Assertion

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [Array.contentEquals].
 */
fun <T> Assertion.Builder<Array<out T>>.contentEquals(other: Array<out T>): Assertion.Builder<Array<out T>> =
  assertThat("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is empty.
 * @see Array.isEmpty
 */
@JvmName("isObjectArrayEmpty")
fun <T> Assertion.Builder<Array<T>>.isEmpty(): Assertion.Builder<Array<T>> =
  assertThat("array is empty") { it.isEmpty() }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [BooleanArray.contentEquals].
 */
fun Assertion.Builder<BooleanArray>.contentEquals(other: BooleanArray): Assertion.Builder<BooleanArray> =
  assertThat("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is empty.
 * @see Array.isEmpty
 */
@JvmName("isBooleanArrayEmpty")
fun Assertion.Builder<BooleanArray>.isEmpty(): Assertion.Builder<BooleanArray> =
  assertThat("array is empty") { it.isEmpty() }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [ByteArray.contentEquals].
 */
fun Assertion.Builder<ByteArray>.contentEquals(other: ByteArray): Assertion.Builder<ByteArray> =
  assertThat("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is empty.
 * @see Array.isEmpty
 */
@JvmName("isByteArrayEmpty")
fun Assertion.Builder<ByteArray>.isEmpty(): Assertion.Builder<ByteArray> =
  assertThat("array is empty") { it.isEmpty() }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [ShortArray.contentEquals].
 */
fun Assertion.Builder<ShortArray>.contentEquals(other: ShortArray): Assertion.Builder<ShortArray> =
  assertThat("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is empty.
 * @see Array.isEmpty
 */
@JvmName("isShortArrayEmpty")
fun Assertion.Builder<ShortArray>.isEmpty(): Assertion.Builder<ShortArray> =
  assertThat("array is empty") { it.isEmpty() }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [IntArray.contentEquals].
 */
fun Assertion.Builder<IntArray>.contentEquals(other: IntArray): Assertion.Builder<IntArray> =
  assertThat("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is empty.
 * @see Array.isEmpty
 */
@JvmName("isIntArrayEmpty")
fun Assertion.Builder<IntArray>.isEmpty(): Assertion.Builder<IntArray> =
  assertThat("array is empty") { it.isEmpty() }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [LongArray.contentEquals].
 */
fun Assertion.Builder<LongArray>.contentEquals(other: LongArray): Assertion.Builder<LongArray> =
  assertThat("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is empty.
 * @see Array.isEmpty
 */
@JvmName("isLongArrayEmpty")
fun Assertion.Builder<LongArray>.isEmpty(): Assertion.Builder<LongArray> =
  assertThat("array is empty") { it.isEmpty() }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [FloatArray.contentEquals].
 */
fun Assertion.Builder<FloatArray>.contentEquals(other: FloatArray): Assertion.Builder<FloatArray> =
  assertThat("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is empty.
 * @see Array.isEmpty
 */
@JvmName("isFloatArrayEmpty")
fun Assertion.Builder<FloatArray>.isEmpty(): Assertion.Builder<FloatArray> =
  assertThat("array is empty") { it.isEmpty() }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [DoubleArray.contentEquals].
 */
fun Assertion.Builder<DoubleArray>.contentEquals(other: DoubleArray): Assertion.Builder<DoubleArray> =
  assertThat("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is empty.
 * @see Array.isEmpty
 */
@JvmName("isDoubleArrayEmpty")
fun Assertion.Builder<DoubleArray>.isEmpty(): Assertion.Builder<DoubleArray> =
  assertThat("array is empty") { it.isEmpty() }

/**
 * Asserts that the subject's content is equal to that of [other] according to
 * [CharArray.contentEquals].
 */
fun Assertion.Builder<CharArray>.contentEquals(other: CharArray): Assertion.Builder<CharArray> =
  assertThat("array content equals %s", other) {
    it.contentEquals(other)
  }

/**
 * Asserts that the subject's content is empty.
 * @see Array.isEmpty
 */
@JvmName("is_ArrayEmpty")
fun Assertion.Builder<CharArray>.isEmpty(): Assertion.Builder<CharArray> =
  assertThat("array is empty") { it.isEmpty() }

/**
 * Maps an array to a list to make it possible to use the iterable matchers
 */
fun <T> Assertion.Builder<Array<T>>.toList(): Assertion.Builder<List<T>> =
  get("as list") { toList() }
