plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm {}

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val coroutinesVersion: String by project

        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

                implementation(project(":brown-api"))
                implementation(project(":brown-cor"))
                implementation(project(":brown-stubs"))
                implementation(project(":brown-common"))
                implementation(project(":brown-repo-stubs"))
                implementation(project(":brown-repo-inmemory"))
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(project(":brown-repo-tests"))
                implementation(project(":brown-repo-stubs"))

                api("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
            }
        }

    }
}
