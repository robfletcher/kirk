package strikt.assertions

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.junit.jupiter.api.assertThrows
import strikt.api.expect
import strikt.fails

@DisplayName("assertions on Iterable")
internal class IterableAssertions {
  @Nested
  @DisplayName("all assertion")
  inner class All {
    @Test
    fun `passes if all elements conform`() {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expect(subject).all {
        isLowerCase()
      }
    }

    @Test
    fun `fails if any element does not conform`() {
      fails {
        val subject = setOf("catflap", "rubberplant", "marzipan")
        expect(subject).all {
          startsWith('c')
        }
      }
    }
  }

  @Nested
  @DisplayName("any assertion")
  inner class Any {
    @Test
    fun `passes if all elements conform`() {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expect(subject).any {
        isLowerCase()
      }
    }

    @Test
    fun `passes if any one element conforms`() {
      val subject = setOf("catflap", "RUBBERPLANT", "MARZIPAN")
      expect(subject).any {
        isLowerCase()
      }
    }

    @Test
    fun `fails if no elements conform`() {
      fails {
        val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
        expect(subject).any {
          isLowerCase()
        }
      }
    }

    @Test
    fun `works with not`() {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expect(subject).not().any {
        isUpperCase()
      }
    }
  }

  @Nested
  @DisplayName("none assertion")
  inner class None {
    @Test
    fun `passes if no elements conform`() {
      val subject = setOf("catflap", "rubberplant", "marzipan")
      expect(subject).none {
        isUpperCase()
      }
    }

    @Test
    fun `fails if some elements conforms`() {
      fails {
        val subject = setOf("catflap", "RUBBERPLANT", "MARZIPAN")
        expect(subject).none {
          isUpperCase()
        }
      }
    }

    @Test
    fun `fails if all elements conform`() {
      fails {
        val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
        expect(subject).none {
          isUpperCase()
        }
      }
    }

    @Test
    fun `works with not`() {
      val subject = setOf("CATFLAP", "RUBBERPLANT", "MARZIPAN")
      expect(subject).not().none {
        isUpperCase()
      }
    }
  }

  @Nested
  @DisplayName("contains assertion")
  inner class Contains {
    @TestFactory
    fun `passes subject contains expected`() =
      listOf(
        Pair(
          listOf("catflap"),
          arrayOf("catflap")
        ),
        Pair(
          listOf("catflap", "rubberplant", "marzipan"),
          arrayOf("catflap")
        ),
        Pair(
          listOf("catflap", "rubberplant", "marzipan"),
          arrayOf("catflap", "marzipan")
        )
      ).map { (subject, expected) ->
        dynamicTest("passes $subject contains ${expected.toList()}") {
          expect(subject).contains(*expected)
        }
      }

    @TestFactory
    fun `fails subject contains expected`() =
      listOf(
        Pair(
          listOf("catflap", "rubberplant", "marzipan"),
          arrayOf("fnord")
        ),
        Pair(
          listOf("catflap", "rubberplant", "marzipan"),
          arrayOf("catflap", "fnord")
        ),
        Pair(emptyList(), arrayOf("catflap"))
      ).map { (subject, expected) ->
        dynamicTest("fails $subject contains ${expected.toList()}") {
          fails {
            expect(subject).contains(*expected)
          }
        }
      }

    @Test
    fun `rejects an empty array of expected elements`() {
      assertThrows<IllegalArgumentException> {
        expect(listOf("catflap", "rubberplant", "marzipan")).contains()
      }
    }

    @Test
    fun `has a nested failure for each missing element`() {
      fails {
        expect(listOf("catflap", "rubberplant", "marzipan"))
          .contains("fnord", "marzipan", "bojack")
      }
    }
  }

  @Nested
  @DisplayName("doesNotContain assertion")
  inner class DoesNotContain {
    @Test
    fun `always passes for an empty subject`() {
      expect(emptyList<String>())
        .doesNotContain("catflap", "rubberplant", "marzipan")
    }

    @TestFactory
    fun `fails if no elements are specified`() =
      listOf(
        emptyList(),
        listOf("catflap", "rubberplant", "marzipan")
      ).map { subject ->
        dynamicTest("fails for $subject is if no elements are specified") {
          assertThrows<IllegalArgumentException> {
            expect(subject).doesNotContain()
          }
        }
      }

    fun `passes if the subject contains none of the elements`() =
      listOf(
        arrayOf("fnord"),
        arrayOf("xenocracy", "wye", "exercitation")
      ).map { elements ->
        dynamicTest("passes if the subject contains none of the elements ${elements.toList()}") {
          expect(listOf("catflap", "rubberplant", "marzipan"))
            .doesNotContain(*elements)
        }
      }

    @TestFactory
    fun `passes if the subject contains any of the elements`() =
      listOf(
        arrayOf("catflap"),
        arrayOf("catflap", "kakistocracy", "impeach"),
        arrayOf("owlbear", "marzipan", "illithid")
      ).map { elements ->
        dynamicTest("passes if the subject contains any of the elements ${elements.toList()}") {
          fails {
            expect(listOf("catflap", "rubberplant", "marzipan"))
              .doesNotContain(*elements)
          }
        }
      }

    @Test
    fun `formats its failure message correctly`() =
      fails {
        expect(listOf("catflap", "rubberplant", "marzipan"))
          .doesNotContain("catflap", "wye", "marzipan")
      }.let { e ->
        assertEquals(
          "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
            "  ✗ does not contain any of the elements [\"catflap\", \"wye\", \"marzipan\"]\n" +
            "    ✗ does not contain \"catflap\"\n" +
            "    ✓ does not contain \"wye\"\n" +
            "    ✗ does not contain \"marzipan\"",
          e.message
        )
      }
  }

  @Nested
  @DisplayName("containsExactly assertion")
  inner class ContainsExactly {
    @Nested
    @DisplayName("a Set subject")
    inner class Set {
      val subject = setOf("catflap", "rubberplant", "marzipan")

      @Test
      fun `passes if the elements are identical`() {
        expect(subject).containsExactly("catflap", "rubberplant", "marzipan")
      }

      @Test
      fun `fails if there are more elements than expected`() {
        fails {
          expect(subject).containsExactly("rubberplant", "catflap")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        fails {
          expect(subject)
            .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
        }
      }

      @Test
      fun `fails if the order is different (even though this is a Set)`() {
        fails {
          expect(subject).containsExactly("rubberplant", "catflap", "marzipan")
        }
      }
    }

    @Nested
    @DisplayName("a List subject")
    inner class List {
      val subject = listOf("catflap", "rubberplant", "marzipan")

      @Test
      fun `passes if all the elements exist in the same order`() {
        expect(subject).containsExactly("catflap", "rubberplant", "marzipan")
      }

      @Test
      fun `fails if there are more elements than expected`() {
        fails {
          expect(subject).containsExactly("catflap", "rubberplant")
        }.let { error ->
          assertEquals(
            "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
              "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\"]\n" +
              "    ✓ contains \"catflap\"\n" +
              "    ✓ …at index 0\n" +
              "    ✓ contains \"rubberplant\"\n" +
              "    ✓ …at index 1\n" +
              "    ✗ contains no further elements : found [\"marzipan\"]",
            error.message
          )
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        fails {
          expect(subject)
            .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
        }.let { error ->
          assertEquals(
            "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
              "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\", \"marzipan\", \"fnord\"]\n" +
              "    ✓ contains \"catflap\"\n" +
              "    ✓ …at index 0\n" +
              "    ✓ contains \"rubberplant\"\n" +
              "    ✓ …at index 1\n" +
              "    ✓ contains \"marzipan\"\n" +
              "    ✓ …at index 2\n" +
              "    ✗ contains \"fnord\"\n" +
              "    ✓ contains no further elements",
            error.message
          )
        }
      }

      @Test
      fun `fails if the order is different`() {
        fails {
          expect(subject).containsExactly("rubberplant", "catflap", "marzipan")
        }.let { error ->
          assertEquals(
            "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
              "  ✗ contains exactly the elements [\"rubberplant\", \"catflap\", \"marzipan\"]\n" +
              "    ✓ contains \"rubberplant\"\n" +
              "    ✗ …at index 0 : found \"catflap\"\n" +
              "    ✓ contains \"catflap\"\n" +
              "    ✗ …at index 1 : found \"rubberplant\"\n" +
              "    ✓ contains \"marzipan\"\n" +
              "    ✓ …at index 2\n" +
              "    ✓ contains no further elements",
            error.message
          )
        }
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        fails {
          expect(subject)
            .containsExactly("catflap", "rubberplant", "marzipan", "marzipan")
        }.let { error ->
          assertEquals(
            "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
              "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\", \"marzipan\", \"marzipan\"]\n" +
              "    ✓ contains \"catflap\"\n" +
              "    ✓ …at index 0\n" +
              "    ✓ contains \"rubberplant\"\n" +
              "    ✓ …at index 1\n" +
              "    ✓ contains \"marzipan\"\n" +
              "    ✓ …at index 2\n" +
              "    ✗ contains \"marzipan\"\n" +
              "    ✓ contains no further elements",
            error.message
          )
        }
      }
    }

    @Nested
    @DisplayName("a non-Collection Iterable subject")
    inner class NonCollection {
      val subject = object : Iterable<String> {
        override fun iterator() =
          arrayOf("catflap", "rubberplant", "marzipan").iterator()
      }

      @Test
      fun `passes if the elements are indentical`() {
        expect(subject)
          .describedAs("a non-Collection iterable %s")
          .containsExactly("catflap", "rubberplant", "marzipan")
      }

      @Test
      fun `fails if the elements are ordered differently`() {
        fails {
          expect(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("marzipan", "rubberplant", "catflap")
        }
      }

      @Test
      fun `fails if there are more elements than expected`() {
        fails {
          expect(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("catflap", "rubberplant")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        fails {
          expect(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("catflap", "rubberplant", "marzipan", "fnord")
        }
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        fails {
          expect(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly("catflap", "rubberplant", "marzipan", "marzipan")
        }
      }

      @Test
      fun `fails if it's supposed to be empty and isn't`() {
        fails {
          expect(subject)
            .describedAs("a non-Collection iterable %s")
            .containsExactly()
        }
      }

      @Test
      fun `passes if it's supposed to be empty and is`() {
        val emptySubject = object : Iterable<String> {
          override fun iterator() = emptySequence<String>().iterator()
        }
        expect(emptySubject).containsExactly()
      }
    }
  }

  @Nested
  @DisplayName("containsExactlyInAnyOrder assertion")
  inner class ContainsExactlyInAnyOrder {
    @Nested
    @DisplayName("a Set subject")
    inner class Set {
      val subject = setOf("catflap", "rubberplant", "marzipan")

      @Test
      fun `passes if the elements are identical`() {
        expect(subject)
          .containsExactlyInAnyOrder("rubberplant", "catflap", "marzipan")
      }

      @Test
      fun `fails if there are more elements than expected`() {
        fails {
          expect(subject).containsExactlyInAnyOrder("rubberplant", "catflap")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        fails {
          expect(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "fnord"
            )
        }
      }
    }

    @Nested
    @DisplayName("a List subject")
    inner class List {
      val subject = listOf("catflap", "rubberplant", "marzipan")

      @Test
      fun `passes if all the elements exist in the same order`() {
        expect(subject)
          .containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan")
      }

      @Test
      fun `fails if there are more elements than expected`() {
        fails {
          expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant")
        }.let { error ->
          assertEquals(
            "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
              "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\"] in any order\n" +
              "    ✓ contains \"catflap\"\n" +
              "    ✓ contains \"rubberplant\"\n" +
              "    ✗ contains no further elements : found [\"marzipan\"]",
            error.message
          )
        }
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        fails {
          expect(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "marzipan"
            )
        }.let { error ->
          assertEquals(
            "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
              "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\", \"marzipan\", \"marzipan\"] in any order\n" +
              "    ✓ contains \"catflap\"\n" +
              "    ✓ contains \"rubberplant\"\n" +
              "    ✓ contains \"marzipan\"\n" +
              "    ✗ contains \"marzipan\"\n" +
              "    ✓ contains no further elements",
            error.message
          )
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        fails {
          expect(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "fnord"
            )
        }.let { error ->
          assertEquals(
            "▼ Expect that [\"catflap\", \"rubberplant\", \"marzipan\"]:\n" +
              "  ✗ contains exactly the elements [\"catflap\", \"rubberplant\", \"marzipan\", \"fnord\"] in any order\n" +
              "    ✓ contains \"catflap\"\n" +
              "    ✓ contains \"rubberplant\"\n" +
              "    ✓ contains \"marzipan\"\n" +
              "    ✗ contains \"fnord\"\n" +
              "    ✓ contains no further elements",
            error.message
          )
        }
      }

      @Test
      fun `passes if the order is different`() {
        expect(subject)
          .containsExactlyInAnyOrder("rubberplant", "catflap", "marzipan")
      }
    }

    @Nested
    @DisplayName("a non-Collection Iterable subject")
    inner class NonCollection {
      val subject = object : Iterable<String> {
        override fun iterator() =
          arrayOf("catflap", "rubberplant", "marzipan").iterator()
      }

      @Test
      fun `passes if the elements are indentical`() {
        expect(subject)
          .containsExactlyInAnyOrder("catflap", "rubberplant", "marzipan")
      }

      @Test
      fun `passes if the elements are ordered differently`() {
        expect(subject)
          .containsExactlyInAnyOrder("marzipan", "rubberplant", "catflap")
      }

      @Test
      fun `fails if there are more elements than expected`() {
        fails {
          expect(subject).containsExactlyInAnyOrder("catflap", "rubberplant")
        }
      }

      @Test
      fun `fails if there are fewer elements than expected`() {
        fails {
          expect(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "fnord"
            )
        }
      }

      @Test
      fun `fails if the cardinality of an element is lower than expected`() {
        fails {
          expect(subject)
            .containsExactlyInAnyOrder(
              "catflap",
              "rubberplant",
              "marzipan",
              "marzipan"
            )
        }
      }

      @Test
      fun `fails if it's supposed to be empty and isn't`() {
        fails {
          expect(subject).containsExactlyInAnyOrder()
        }
      }

      @Test
      fun `passes if it's supposed to be empty and is`() {
        val emptySubject = object : Iterable<String> {
          override fun iterator() = emptySequence<String>().iterator()
        }
        expect(emptySubject).containsExactlyInAnyOrder()
      }
    }
  }
}
