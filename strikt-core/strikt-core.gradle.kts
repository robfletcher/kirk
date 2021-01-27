plugins {
  kotlin("jvm")
  id("published")
  id("info.solidsoft.pitest")
}

dependencies {
  api("org.opentest4j:opentest4j:+")

  implementation("com.christophsturm:filepeek:+")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")

  testImplementation("dev.minutest:minutest:+")
  testImplementation("com.christophsturm:failfast:0.2.0")

}
