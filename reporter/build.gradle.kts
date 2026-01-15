plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":common"))

    // WebFlux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // JPA (리포트 조회용)
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2")

    // RabbitMQ (Reactor)
    implementation("io.projectreactor.rabbitmq:reactor-rabbitmq:1.5.6")
}
