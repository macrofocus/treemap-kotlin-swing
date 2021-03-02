//enableFeaturePreview("GRADLE_METADATA")
pluginManagement {
    val kotlinVersion: String by settings

    plugins {
        kotlin("jvm") version kotlinVersion apply false
    }
}