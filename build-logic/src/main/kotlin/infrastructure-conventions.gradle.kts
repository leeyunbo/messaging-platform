plugins {
    id("kotlin-conventions")
    id("org.jetbrains.kotlin.plugin.spring")
    `java-library`
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
}
