plugins {
    `java-library`
}

dependencies {
    api(platform(rootProject.libs.spring.boot.bom))

    api(rootProject.libs.spring.boot.starter.web)
    api(rootProject.libs.spring.boot.starter.validation)
    api(rootProject.libs.logstash.logback.encoder)

    testImplementation(rootProject.libs.spring.boot.starter.test)
}