plugins {
    id("usecase-conventions")
}

dependencies {
    implementation(project(":message-core:sms-domain"))
    implementation(project(":message-library:logging"))
}
