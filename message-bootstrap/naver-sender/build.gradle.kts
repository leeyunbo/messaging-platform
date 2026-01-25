plugins {
    id("bootstrap-conventions")
}

dependencies {
    implementation(project(":message-core:naver-domain"))
    implementation(project(":message-core:partner-domain"))
    implementation(project(":message-infrastructure:rabbitmq"))
    implementation(project(":message-infrastructure:db-naver"))
    implementation(project(":message-library:id-generator"))
    implementation(project(":message-library:logging"))
    implementation(project(":message-platform:naver-platform"))
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
