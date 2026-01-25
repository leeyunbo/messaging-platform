plugins {
    id("infrastructure-conventions")
}

dependencies {
    implementation(project(":message-core:naver-domain"))

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
}
