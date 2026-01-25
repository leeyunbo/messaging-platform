plugins {
    id("infrastructure-conventions")
}

dependencies {
    implementation(project(":message-core:sms-domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
}
