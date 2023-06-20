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

val openapiGroup = "${group}.api.v1"

val apiModelOutputDir = "$rootDir/brown-api/src/main/kotlin/${openapiGroup.replace('.', '/')}/models"

/**
 * Settings for model generation
 */
openApiGenerate {
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
    ignoreFileOverride.set("$apiModelOutputDir/.ignored")
    /**
     * Some models have to be processed after generation. The setting below (along with corresponding env variable)
     * was intended for that but it seems it doesn't work. So, I created postprocessing gradle task instead (at the end of this file).
     */
    enablePostProcessFile.set(true)

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
    register<ApiModelPostProcessor>("apiModelPostProcessor") {
        apiModelDir.set(file(apiModelOutputDir))
        discriminatorNameList.set(listOf("requestType", "responseType"))
        excludedClassNameList.set(listOf("IRequest", "IResponse"))
    }
    compileKotlin {
        dependsOn(openApiGenerate, apiModelPostProcessor)
    }
    /**
     * Processing of files should begin right after they are generated
     */
    apiModelPostProcessor {
        dependsOn(openApiGenerate)
    }
}

/**
 * Task for postprocessing of generated api models (see README.md in $outputDir)
 */
val TaskContainer.apiModelPostProcessor
    get() = named<ApiModelPostProcessor>("apiModelPostProcessor")

abstract class ApiModelPostProcessor : DefaultTask() {
    /**
     * Directory where generated models reside
     */
    @get:OutputDirectory
    abstract val apiModelDir: DirectoryProperty

    /**
     * List of discriminator names to process in generated models
     */
    @get:Input
    abstract val discriminatorNameList: ListProperty<String>

    /**
     * List of excluded classes with discriminator to prevent processing.
     * Typically, these are interfaces with discriminators to be kept as is.
     */
    @get:Input
    abstract val excludedClassNameList: ListProperty<String>

    @TaskAction
    fun execute() {
        apiModelDir.asFileTree.forEach { it.removeDuplicatedProperty() }
    }

    private fun File.removeDuplicatedProperty() {
        if (isDirectory || excludedClassNameList.get().contains(nameWithoutExtension)) return
        discriminatorNameList.get().forEach {
            val fileContent = readText().replace(
                """("$it")""",
                """("$it", access = JsonProperty.Access.WRITE_ONLY)"""
            )
            writeText(fileContent)
        }
    }
}
