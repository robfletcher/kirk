package strikt

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import strikt.api.expect
import strikt.assertions.contains
import strikt.assertions.isA
import strikt.assertions.isNotNull
import strikt.assertions.isNull

@DisplayName("assertions in blocks")
internal class Block {
  @Test
  fun `all assertions in a block are evaluated even if some fail`() {
    fails {
      val subject: Any? = "fnord"
      expect(subject) {
        isNull()
        isNotNull()
        isA<String>()
        isA<Number>()
      }
    }.let { error ->
      val expected = "▼ Expect that \"fnord\":\n" +
        "  ✗ is null\n" +
        "  ✓ is not null\n" +
        "  ✓ is an instance of java.lang.String\n" +
        "  ✗ is an instance of java.lang.Number : found java.lang.String"
      assertEquals(expected, error.message)
    }
  }

  @Test
  fun `assertions in a block can be negated`() {
    fails {
      val subject: Any? = "fnord"
      expect(subject) {
        not().isNull()
        not().isNotNull()
        not().isA<String>()
        not().isA<Number>()
      }
    }.let { error ->
      val expected = "▼ Expect that \"fnord\":\n" +
        "  ✓ is not null\n" +
        "  ✗ is null\n" +
        "  ✗ is not an instance of java.lang.String\n" +
        "  ✓ is not an instance of java.lang.Number"
      assertEquals(expected, error.message)
    }
  }

  @Test
  fun `an and block can be negated`() {
    val subject: Any? = "fnord"
    expect(subject).not().and {
      isNull()
    }
  }

  @Test
  fun `contains can be negated`() {
    expect(listOf<String>()).not().contains("blah")
  }
}
