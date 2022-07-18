import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
    kotlin("plugin.jpa") version "1.6.21"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

val akkaVersion = "2.6.19"
val scalaBinary = "2.13"
val akkaPlatform = "0.2.7"


dependencies {

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.liquibase:liquibase-core")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("org.postgresql:postgresql")
    testImplementation("org.springframework.boot:spring-boot-starter-test")


    implementation(platform("com.typesafe.akka:akka-bom_${scalaBinary}:${akkaVersion}"))
    implementation(platform("com.lightbend.akka:akka-platform-dependencies_${scalaBinary}:${akkaPlatform}"))
    implementation("com.typesafe.akka:akka-actor-typed_${scalaBinary}")
    implementation("com.typesafe.akka:akka-persistence-typed_${scalaBinary}")
    implementation("com.typesafe.akka:akka-persistence-query_${scalaBinary}")
    implementation("com.lightbend.akka:akka-persistence-jdbc_${scalaBinary}")
    implementation("com.lightbend.akka:akka-projection-eventsourced_${scalaBinary}")
    implementation("com.lightbend.akka:akka-projection-jdbc_${scalaBinary}")
    implementation("com.lightbend.akka.management:akka-management-cluster-http_${scalaBinary}")
    implementation("com.lightbend.akka.management:akka-management-cluster-bootstrap_${scalaBinary}")
    implementation("com.typesafe.akka:akka-cluster-sharding-typed_${scalaBinary}")
    implementation("com.typesafe.akka:akka-serialization-jackson_${scalaBinary}")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
