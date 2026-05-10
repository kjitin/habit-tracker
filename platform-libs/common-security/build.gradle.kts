plugins {
    `java-library`
}

dependencies {
    api(platform(rootProject.libs.spring.boot.bom))

    api(project(":platform-libs:common-web"))
    api("org.springframework.boot:spring-boot-starter-security")

    api(rootProject.libs.jjwt.api)
    runtimeOnly(rootProject.libs.jjwt.impl)
    runtimeOnly(rootProject.libs.jjwt.jackson)

    testImplementation(rootProject.libs.spring.boot.starter.test)
    testImplementation("org.springframework.security:spring-security-test")
}