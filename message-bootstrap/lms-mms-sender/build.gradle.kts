plugins {
    id("bootstrap-conventions")
}

dependencies {
    implementation(project(":message-core:partner-domain"))
    implementation(project(":message-infrastructure:rabbitmq"))
    implementation(project(":message-infrastructure:db-sms"))
    implementation(project(":message-library:id-generator"))
    implementation(project(":message-library:logging"))
    implementation(project(":message-platform:skt-platform"))
    implementation(project(":message-platform:kt-platform"))
    implementation(project(":message-platform:lgt-platform"))
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
