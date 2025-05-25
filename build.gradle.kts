plugins {
    kotlin("jvm")
    application
}

//val local = Properties()
//val localProperties: File = rootProject.file("local.properties")
//if (localProperties.exists()) {
//    localProperties.inputStream().use { local.load(it) }
//}

tasks.compileJava {
    options.release.set(21)
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    compilerOptions.jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
}

repositories {
    maven { url = uri("https://archiva.macrofocus.com/repository/public/") }
    maven { url = uri("https://archiva.macrofocus.com/repository/snapshots/") }
    mavenCentral()

    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev/")

    google()
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

val frameworkAttribute = Attribute.of("mkui", String::class.java)
configurations.all {
    afterEvaluate {
        attributes.attribute(frameworkAttribute, "swing")
    }
}

dependencies {
    val localDependencies: String? by project
    if(localDependencies != null && localDependencies.toBoolean()) {
        implementation(project(":macrofocus-common"))
        implementation(project(":molap"))
        implementation(project(":mkui"))
        implementation(project(":treemap"))
    } else {
        val macrofocusVersion: String by project
        implementation("org.macrofocus:macrofocus-common:$macrofocusVersion")
        implementation("org.molap:molap:$macrofocusVersion")
        implementation("org.mkui:mkui-swing:$macrofocusVersion")
        implementation("com.treemap:treemap-swing:$macrofocusVersion")
    }
}

application {
    mainClass.set("Demo")
}

distributions {
    main {
//        distributionBaseName.set("someName")
        contents {
            from(".") {
                exclude(".gradle/**")
                exclude("build/**")
            }

//            from("TreeMap API for Kotlin-Swing Developer Guide.pdf")
//            from("../treemap/build/dokka/html/") {
//                into("dokka")
//            }
        }
    }
}