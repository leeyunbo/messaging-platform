plugins {
    id("platform-conventions")
}

dependencies {
    implementation(project(":message-core:sms-domain"))
    implementation(project(":message-infrastructure:webclient"))
    implementation(project(":message-library:logging"))
}
