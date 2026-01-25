plugins {
    id("usecase-conventions")
}

dependencies {
    implementation(project(":message-core:rcs-domain"))
    implementation(project(":message-library:logging"))
}
