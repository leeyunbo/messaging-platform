plugins {
    id("bootstrap-conventions")
}

dependencies {
    implementation(project(":message-core:sms-domain"))
    implementation(project(":message-core:partner-domain"))
    implementation(project(":message-infrastructure:rabbitmq"))
    implementation(project(":message-infrastructure:db-sms"))
    implementation(project(":message-library:id-generator"))
    implementation(project(":message-library:logging"))

    implementation("org.springframework.boot:spring-boot-starter-validation")
}

tasks.bootJar {
    archiveBaseName.set("sms-receiver")
}
