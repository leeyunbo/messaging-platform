plugins {
    id("bootstrap-conventions")
}

dependencies {
    implementation(project(":message-core:kakao-domain"))
    implementation(project(":message-usecase:kakao-alimtalk-usecase"))
    implementation(project(":message-infrastructure:rabbitmq"))
    implementation(project(":message-infrastructure:db-kakao"))
    implementation(project(":message-platform:kakao-platform"))
    implementation(project(":message-library:logging"))

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("io.r2dbc:r2dbc-h2")
}

tasks.bootJar {
    archiveBaseName.set("kakao-alimtalk-sender")
}
