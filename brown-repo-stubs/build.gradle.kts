plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val coroutinesVersion: String by project

        val commonMain by getting {
            dependencies {
                implementation(project(":brown-stubs"))
                implementation(project(":brown-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":brown-repo-tests"))
            }
        }
    }
}
