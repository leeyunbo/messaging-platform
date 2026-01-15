plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.jpa")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":common"))
    implementation(project(":provider"))

    // WebFlux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // JPA + DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("com.mysql:mysql-connector-j")
    runtimeOnly("com.h2database:h2") // 테스트용

    // RabbitMQ (Reactor)
    implementation("io.projectreactor.rabbitmq:reactor-rabbitmq:1.5.6")
}
