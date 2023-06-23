plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("org.openapi.generator")
}

kotlin {
    jvm { }

    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val coroutinesVersion: String by project
        val serializationVersion: String by project

        val commonMain by getting {
//            kotlin.srcDirs("$buildDir/generate-resources/main/src/commonMain/kotlin")
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:$serializationVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}

val openapiGroup = "${group}.api.log"
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
    outputDir.set("$rootDir/brown-log-api")
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
    inputSpec.set("$rootDir/specs/log.yaml")
    /**
     * Use library for KMP
     */
    library.set("multiplatform")

    /**
     * We need models only
     * https://openapi-generator.tech/docs/globals
     */
    globalProperties.apply {
        put("models", "")
        put("modelDocs", "false")
    }

    /**
     * Настройка дополнительных параметров из документации по генератору
     * https://github.com/OpenAPITools/openapi-generator/blob/master/docs/generators/kotlin.md
     */
    configOptions.set(
        mapOf(
            "dateLibrary" to "string",
            "enumPropertyNaming" to "UPPERCASE",
            "collectionType" to "list"
        )
    )
}

afterEvaluate {
    val openApiGenerate = tasks.getByName("openApiGenerate")
    tasks.filter { it.name.startsWith("compile") }.forEach {
        it.dependsOn(openApiGenerate)
    }
}
