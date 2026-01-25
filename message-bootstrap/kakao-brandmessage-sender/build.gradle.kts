plugins {
    id("bootstrap-conventions")
}

dependencies {
    implementation(project(":message-core:kakao-domain"))
    implementation(project(":message-core:partner-domain"))
    implementation(project(":message-usecase:kakao-brandmessage-usecase"))
    implementation(project(":message-infrastructure:rabbitmq"))
    implementation(project(":message-platform:kakao-direct-platform"))
    implementation(project(":message-library:id-generator"))
    implementation(project(":message-library:logging"))
}

tasks.bootJar {
    archiveBaseName.set("kakao-brandmessage-sender")
}
