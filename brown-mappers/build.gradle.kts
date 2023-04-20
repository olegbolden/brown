plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":brown-api"))
    implementation(project(":brown-common"))

    testImplementation(kotlin("test-junit"))
}
