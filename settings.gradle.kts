rootProject.name = "brown"

pluginManagement {
    val kotlinVersion: String by settings
    val apiSpecVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("org.openapi.generator") version apiSpecVersion apply false
    }
}

//include("m1l1")
include("brown-api")
include("brown-mappers")
include("brown-common")
