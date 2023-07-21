plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val cache4kVersion: String by project
        val coroutinesVersion: String by project
        val kmpUUIDVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(project(":brown-common"))
                implementation(project(":brown-repo-tests"))

                implementation("com.benasher44:uuid:$kmpUUIDVersion")
                implementation("io.github.reactivecircus.cache4k:cache4k:$cache4kVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}
