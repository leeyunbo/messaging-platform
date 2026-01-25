plugins {
    id("bootstrap-conventions")
}

dependencies {
    implementation(project(":message-core:partner-domain"))
    implementation(project(":message-core:sms-domain"))
    implementation(project(":message-core:kakao-domain"))
    implementation(project(":message-core:rcs-domain"))
    implementation(project(":message-core:naver-domain"))
    implementation(project(":message-infrastructure:rabbitmq"))
    implementation(project(":message-infrastructure:db"))
    implementation(project(":message-infrastructure:webclient"))
    implementation(project(":message-library:logging"))

    implementation("io.github.resilience4j:resilience4j-retry")
}

tasks.bootJar {
    archiveBaseName.set("reporter")
}
