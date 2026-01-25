plugins {
    id("bootstrap-conventions")
}

dependencies {
    implementation(project(":message-core:rcs-domain"))
    implementation(project(":message-core:partner-domain"))
    implementation(project(":message-infrastructure:rabbitmq"))
    implementation(project(":message-infrastructure:db-rcs"))
    implementation(project(":message-library:id-generator"))
    implementation(project(":message-library:logging"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
