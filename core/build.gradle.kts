plugins {
    kotlin("jvm")
}

group = "org.ledgers"
version = rootProject.version

java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.assertj:assertj-core:3.24.2") // also present in spring-boot-starter-test
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1") // also present in spring-boot-starter-test
}

tasks.withType<Test> {
    useJUnitPlatform()
}

