plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

dependencies {
    implementation(project(":platform-libs:common-web"))
    implementation(project(":platform-libs:common-security"))

    implementation(rootProject.libs.spring.boot.starter.data.jpa)
    implementation(rootProject.libs.spring.boot.starter.actuator)
    implementation(rootProject.libs.flyway.core)
    implementation(rootProject.libs.flyway.database.postgresql)
    runtimeOnly(rootProject.libs.postgresql)

    implementation(rootProject.libs.spring.cloud.aws.starter.sqs)
    implementation(rootProject.libs.micrometer.registry.prometheus)

    testImplementation(rootProject.libs.spring.boot.starter.test)
    testImplementation(rootProject.libs.spring.security.test)
    testImplementation(rootProject.libs.spring.boot.testcontainers)
    testImplementation(rootProject.libs.testcontainers.postgresql)
    testImplementation(rootProject.libs.testcontainers.junit)
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = true
    archiveClassifier.set("")
    archiveFileName.set("habit-service.jar")
}

tasks.named<Jar>("jar") {
    enabled = false
}
