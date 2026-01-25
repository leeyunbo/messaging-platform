plugins {
    id("kotlin-conventions")
    id("org.jetbrains.kotlin.plugin.spring")
    `java-library`
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.github.resilience4j:resilience4j-circuitbreaker")
    implementation("io.github.resilience4j:resilience4j-reactor")
    implementation("tools.jackson.core:jackson-databind")
    implementation("tools.jackson.module:jackson-module-kotlin")
}
