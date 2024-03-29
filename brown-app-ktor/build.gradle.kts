@file:Suppress("UNUSED_VARIABLE")

import org.jetbrains.kotlin.util.suffixIfNot

val ktorVersion: String by project
val logbackVersion: String by project
val serializationVersion: String by project
val flywayDbVersion: String by project

// ex: Converts to "io.ktor:ktor-ktor-server-netty:2.0.1" with only ktor("netty")
fun ktor(
    module: String,
    prefix: String = "server-",
    version: String? = this@Build_gradle.ktorVersion
): Any = "io.ktor:ktor-${prefix.suffixIfNot("-")}$module:$version"

plugins {
    id("application")
    kotlin("plugin.serialization")
    kotlin("multiplatform")
    id("io.ktor.plugin")
    id("org.flywaydb.flyway")
}

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
}

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

ktor {
    docker {
        localImageName.set(project.name + "-ktor")
        imageTag.set(project.version.toString())
        jreVersion.set(io.ktor.plugin.features.JreVersion.JRE_17)
    }
}

jib {
    container.mainClass = "io.ktor.server.netty.EngineMain"
}

kotlin {
    jvm {
        withJava()
    }

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "ru.otus.kotlin.brown.app.ktor.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
                implementation(ktor("core")) // "io.ktor:ktor-server-core:$ktorVersion"
                implementation(ktor("netty")) // "io.ktor:ktor-ktor-server-netty:$ktorVersion"

                // jackson
                implementation(ktor("jackson", "serialization")) // io.ktor:ktor-serialization-jackson
                implementation(ktor("content-negotiation")) // io.ktor:ktor-server-content-negotiation
                implementation(ktor("kotlinx-json", "serialization")) // io.ktor:ktor-serialization-kotlinx-json

                implementation(ktor("locations"))
                implementation(ktor("caching-headers"))
                implementation(ktor("call-logging"))
                implementation(ktor("auto-head-response"))
                implementation(ktor("cors")) // "io.ktor:ktor-cors:$ktorVersion"
                implementation(ktor("default-headers")) // "io.ktor:ktor-cors:$ktorVersion"

                implementation(ktor("websockets")) // "io.ktor:ktor-websockets:$ktorVersion"
                implementation(ktor("auth")) // "io.ktor:ktor-auth:$ktorVersion"
                implementation(ktor("auth-jwt")) // "io.ktor:ktor-auth-jwt:$ktorVersion"

                implementation("com.zaxxer:HikariCP:5.0.1")
                implementation("org.flywaydb:flyway-core:$flywayDbVersion")

                implementation("ch.qos.logback:logback-classic:$logbackVersion")

                implementation(project(":brown-biz"))
                implementation(project(":brown-stubs"))
                implementation(project(":brown-common"))

                implementation(project(":brown-api"))
                implementation(project(":brown-mappers"))

                implementation(project(":brown-log-logback"))
                implementation(project(":brown-log-mappers"))

                implementation(project(":brown-repo-stubs"))
                implementation(project(":brown-repo-inmemory"))
                implementation(project(":brown-repo-postgresql"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(ktor("test-host")) // "io.ktor:ktor-server-test-host:$ktorVersion"
                implementation(ktor("content-negotiation", prefix = "client-"))
                implementation(ktor("websockets", prefix = "client-"))
                implementation(project(":brown-repo-tests"))
            }
        }
    }
}

tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
