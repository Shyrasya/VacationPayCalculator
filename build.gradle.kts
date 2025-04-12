plugins {
    java
    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.1.7"
    id("application")
}

version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation ("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.mockito:mockito-junit-jupiter")
}

application {
    mainClass.set("neoflex.VacationPayCalculatorApplication")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    archiveBaseName.set("vacation-pay-calculator")
    archiveVersion.set("1.0")
    archiveClassifier.set("")
    manifest {
        attributes("Main-Class" to "neoflex.VacationPayCalculatorApplication")
    }
}