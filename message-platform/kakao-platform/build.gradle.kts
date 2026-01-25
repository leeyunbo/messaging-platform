plugins {
    id("platform-conventions")
}

dependencies {
    implementation(project(":message-core:kakao-domain"))
    implementation(project(":message-infrastructure:webclient"))
    implementation(project(":message-library:logging"))
    implementation(project(":message-library:id-generator"))
}
