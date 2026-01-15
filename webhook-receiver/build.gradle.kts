plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":common"))

    // WebFlux
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    // RabbitMQ (결과를 reporter로 전달)
    implementation("io.projectreactor.rabbitmq:reactor-rabbitmq:1.5.6")
}
