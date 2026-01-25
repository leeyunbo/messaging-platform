plugins {
    id("infrastructure-conventions")
}

dependencies {
    implementation(project(":message-core:partner-domain"))
    implementation(project(":message-core:sms-domain"))
    implementation(project(":message-core:kakao-domain"))
    implementation(project(":message-core:rcs-domain"))
    implementation(project(":message-core:naver-domain"))
    implementation(project(":message-core:report-domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.postgresql:r2dbc-postgresql")
    implementation("tools.jackson.core:jackson-databind")
    implementation("tools.jackson.module:jackson-module-kotlin")
}
