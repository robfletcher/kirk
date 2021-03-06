
# Strikt

Strikt is an assertion library for Kotlin intended for use with a test runner such as [JUnit](https://junit.org/junit5/), [Minutest](https://github.com/dmcg/minutest), [Spek](http://spekframework.org/), or [KotlinTest](https://github.com/kotlintest/kotlintest).

Strikt uses a fluent assertion style similar to [AssertJ](https://assertj.github.io/doc/) but leverages Kotlin's type system and extension functions rather than needing a complex hierarchy of assertion builder classes.

Strikt is under development, but 100% usable.
The API may change until a [version 1.0](https://github.com/robfletcher/strikt/milestone/1) is released.
Any suggestions, [issue reports](https://github.com/robfletcher/strikt/issues), [contributions](https://github.com/robfletcher/strikt/pulls), or feedback are very welcome.

## Installation

Strikt is available from Maven Central.

```kotlin
repositories {
  mavenCentral()
}

dependencies {
  testImplementation("io.strikt:strikt-core:<version>")
}
```

See the button below or [releases/latest](https://github.com/robfletcher/strikt/releases/latest) for the current version number.

## Additional Libraries

Strikt has the following additional libraries:

* `strikt-arrow` -- supports data types from the [Arrow](https://arrow-kt.io/) functional programming library.
* `strikt-jackson` -- supports the Jackson JSON library.
* `strikt-jvm` -- supports types from the Java SDK.
* `strikt-mockk` -- supports types from the [MockK](https://mockk.io/) library.
* `strikt-protobuf` -- supports Protobuf / gRPC.
* `strikt-spring` -- supports the Spring Framework.

Versions are synchronized with the core Strikt library.

To install additional libraries include dependencies in your Gradle build.
For example:

```kotlin
dependencies {
  testImplementation("io.strikt:strikt-jvm:<version>")
}
```

## Bill of Materials

Strikt supplies a BOM that is useful for aligning versions when using more than one Strikt module.

```kotlin
dependencies {
  // BOM dependency
  testImplementation(platform("io.strikt:strikt-bom:<version>"))

  // Versions can be omitted as they are supplied by the BOM
  testImplementation("io.strikt:strikt-jackson")
  testImplementation("io.strikt:strikt-jvm")
  testImplementation("io.strikt:strikt-spring")
}
```

## Using Strikt

Please see the [project documentation](https://strikt.io/) and [API docs](https://strikt.io/modules/core/).

## Community

Join the [**#strikt**](https://kotlinlang.slack.com/messages/CAR7KJ96J) channel on the Kotlin Slack.

Follow [**@stri_kt**](https://twitter.com/stri_kt) on Twitter for updates and release notifications.

![Maven Central](https://img.shields.io/maven-central/v/io.strikt/strikt-bom?style=for-the-badge)
[![GitHub Release Date](https://img.shields.io/github/release-date/robfletcher/strikt.svg?style=for-the-badge)](https://github.com/robfletcher/strikt/)
[![license](https://img.shields.io/github/license/robfletcher/strikt.svg?style=for-the-badge&logo=Apache)](https://www.apache.org/licenses/LICENSE-2.0.html)
[![GitHub issues](https://img.shields.io/github/issues/robfletcher/strikt.svg?style=for-the-badge&logo=Github)](https://github.com/robfletcher/strikt/issues)
![GitHub Workflow Status (branch)](https://img.shields.io/github/workflow/status/robfletcher/strikt/CI/master?style=for-the-badge)
![GitHub top language](https://img.shields.io/github/languages/top/robfletcher/strikt.svg?style=for-the-badge&logo=Kotlin&logoColor=white)
[![Twitter Follow](https://img.shields.io/twitter/follow/stri_kt.svg?style=for-the-badge&label=Twitter&logo=Twitter&logoColor=white)](https://twitter.com/stri_kt)
