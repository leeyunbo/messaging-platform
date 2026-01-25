plugins {
    id("kotlin-conventions")
    id("org.jetbrains.kotlin.plugin.spring")
}

dependencies {
    implementation("org.springframework:spring-context")
    implementation("io.github.resilience4j:resilience4j-retry")
    implementation("io.github.resilience4j:resilience4j-kotlin")
}
