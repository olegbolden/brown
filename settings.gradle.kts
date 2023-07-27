rootProject.name = "brown"

pluginManagement {
    val kotlinVersion: String by settings
    val apiSpecVersion: String by settings
    val ktorVersion: String by settings
    val flywayDbVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
        kotlin("plugin.serialization") version kotlinVersion apply false
        id("io.ktor.plugin") version ktorVersion apply false
        id("org.openapi.generator") version apiSpecVersion apply false
        id("org.flywaydb.flyway") version flywayDbVersion apply false
    }
}

include("brown-api")
include("brown-common")
include("brown-mappers")

include("brown-log-api")
include("brown-log-logback")
include("brown-log-common")
include("brown-log-mappers")

include("brown-cor")
include("brown-biz")
include("brown-stubs")
include("brown-app-ktor")

include("brown-repo-tests")
include("brown-repo-stubs")
include("brown-repo-inmemory")
include("brown-repo-postgresql")
