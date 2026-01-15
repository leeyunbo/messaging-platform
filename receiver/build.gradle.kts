plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":common"))

    // WebFlux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // Security (JWT)
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

    // RabbitMQ (Reactor)
    implementation("io.projectreactor.rabbitmq:reactor-rabbitmq:1.5.6")

    // Redis (멱등성 체크)
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
}
