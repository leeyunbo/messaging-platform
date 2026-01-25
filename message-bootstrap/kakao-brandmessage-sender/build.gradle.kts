plugins {
    id("bootstrap-conventions")
}

dependencies {
    implementation(project(":message-core:kakao-domain"))
    implementation(project(":message-core:partner-domain"))
    implementation(project(":message-usecase:kakao-direct-usecase"))
    implementation(project(":message-infrastructure:rabbitmq"))
    implementation(project(":message-infrastructure:db"))
    implementation(project(":message-infrastructure:webclient"))
    implementation(project(":message-platform:kakao-direct-platform"))
    implementation(project(":message-library:id-generator"))
    implementation(project(":message-library:logging"))
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
