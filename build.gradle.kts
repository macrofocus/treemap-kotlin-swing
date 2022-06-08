import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
}

//val local = Properties()
//val localProperties: File = rootProject.file("local.properties")
//if (localProperties.exists()) {
//    localProperties.inputStream().use { local.load(it) }
//}
//val jdk8Home = local["jdk8Home"] as String?
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions.jvmTarget = "1.8"
//if (jdk8Home != null) {
//    compileKotlin.kotlinOptions.jdkHome = jdk8Home
//}

repositories {
    maven { url = uri("https://www.macrofocus.com/archiva/repository/public/") }
    maven { url = uri("https://www.macrofocus.com/archiva/repository/snapshots/") }
    mavenCentral()

    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev/")

    google()
}

val frameworkAttribute = Attribute.of("mkui", String::class.java)
configurations.all {
    afterEvaluate {
        attributes.attribute(frameworkAttribute, "swing")
    }
}

dependencies {
    val macrofocusVersion: String by project
//    implementation("org.macrofocus", "macrofocus-common", "$macrofocusVersion", "jvmRuntime")
//    implementation("org.molap", "molap", "$macrofocusVersion", "jvmRuntime")
//    implementation("org.macrofocus", "mkui", "$macrofocusVersion", "swingRuntime")
//    implementation("com.treemap", "treemap", "$macrofocusVersion", "swingRuntime")

    implementation("org.macrofocus:macrofocus-common:$macrofocusVersion")
    implementation("org.molap:molap:$macrofocusVersion")
    implementation("org.macrofocus:mkui:$macrofocusVersion")
    implementation("com.treemap:treemap:$macrofocusVersion")
}

application {
    mainClass.set("Demo")
}

distributions {
    main {
//        distributionBaseName.set("someName")
        contents {
            from(".") {
                exclude("build/**")
            }

//            from("TreeMap API for Kotlin-Swing Developer Guide.pdf")
            from("../treemap/build/dokka/html/") {
                into("dokka")
            }
        }
    }
}