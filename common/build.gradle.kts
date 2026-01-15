plugins {
    kotlin("jvm")
    kotlin("plugin.jpa")
}

dependencies {
    // JPA (Entity 정의용)
    api("org.springframework.boot:spring-boot-starter-data-jpa")

    // Validation
    api("org.springframework.boot:spring-boot-starter-validation")

    // Jackson (DTO 직렬화)
    api("com.fasterxml.jackson.module:jackson-module-kotlin")

    // TSID (채번용)
    api("io.hypersistence:hypersistence-tsid:2.1.1")
}

// JPA Entity를 위한 all-open 설정
allOpen {
    annotation("jakarta.persistence.Entity")
    annotation("jakarta.persistence.MappedSuperclass")
    annotation("jakarta.persistence.Embeddable")
}
