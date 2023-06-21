rootProject.name = "brown"

pluginManagement {
    val kotlinVersion: String by settings
    val apiSpecVersion: String by settings
    val ktorVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("io.ktor.plugin") version ktorVersion apply false
        id("org.openapi.generator") version apiSpecVersion apply false
    }
}

//include("m1l1")
include("brown-api")
include("brown-mappers")
include("brown-common")
include("brown-app-ktor")
include("brown-biz")
include("brown-stubs")
include("brown-cor")
