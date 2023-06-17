plugins {
    kotlin("jvm")
    id("org.openapi.generator")
}

dependencies {
    val jacksonVersion: String by project
    implementation(kotlin("stdlib"))
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion")
    testImplementation(kotlin("test-junit"))
}

/**
 * This path will be set as a source route for generated files in Idea (F4)
 * This isn't path to place generated models (see openApiGenerate.outputDir below).
 * Not needed in case you put your generated models into regular src directory for your convenience
 */
//sourceSets {
//    main {
//        java.srcDir("$buildDir/generate-resources/main/src/main/kotlin")
//    }
//}

/**
 * Settings for models generation
 */
openApiGenerate {

    val openapiGroup = "${group}.api.v1"
    /**
     * Language specific mode in which models are to be generated (here - client mode for kotlin)
     */
    generatorName.set("kotlin")
    /**
     * If not specified then defaults to "build/generate-resources/main/src/main/kotlin".
     */
    outputDir.set("$rootDir/brown-api")
    /**
     * Base package name from the root directory, specified by outputDir directive
     */
    packageName.set(openapiGroup)
    /**
     * Name of the package for API models from the root directory, specified by outputDir directive
     */
    apiPackage.set("${openapiGroup}.models")
    /**
     * Location of OpenApi specification file
     */
    inputSpec.set("$rootDir/specs/v1.yaml")
    /**
     * List of class files to be skipped on generation.This list must reside in the directory with generated models.
     * Comment this setting out for the first run and put it back if some files was modified by hand and should not be overwritten.
     * There are modelFilesConstrainedTo and apiFilesConstrainedTo parameters for the same purpose, but they don't work for unknown reason.
     */
    ignoreFileOverride.set("$rootDir/brown-api/src/main/kotlin/ru/otus/kotlin/brown/api/v1/models/.ignored")

    /**
     * We need models only
     * https://openapi-generator.tech/docs/globals
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Additional parameter settings
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "serializationLibrary" to "jackson",
            "collectionType" to "list"
        )
    )
}

/**
 * Task below allows auto generation of models upon every run of any gradle task.
 * If not needed, comment it out to prevent auto generation.
 * To generate models manually select openapi tools group in Idea Gradle widget
 * for this subproject and run openApiGenerate task.
 */

tasks {
    compileKotlin {
        dependsOn(openApiGenerate)
    }
}
