plugins {
    kotlin("jvm")
    application
}

dependencies {
    testImplementation(kotlin("test-junit"))
}

application {
    mainClass.set("Main")
}
