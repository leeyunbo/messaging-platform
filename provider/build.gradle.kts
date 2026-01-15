plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":common"))

    // WebFlux (WebClient for provider 호출)
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}
