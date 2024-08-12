plugins {
    kotlin("jvm")
    application
}

//val local = Properties()
//val localProperties: File = rootProject.file("local.properties")
//if (localProperties.exists()) {
//    localProperties.inputStream().use { local.load(it) }
//}

repositories {
    maven { url = uri("https://www.macrofocus.com/archiva/repository/public/") }
    maven { url = uri("https://www.macrofocus.com/archiva/repository/snapshots/") }
    mavenCentral()

    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev/")

    google()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

val frameworkAttribute = Attribute.of("mkui", String::class.java)
configurations.all {
    afterEvaluate {
        if (swingEnabled != "false") {
            attributes.attribute(frameworkAttribute, "swing")
        }
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
        implementation("org.mkui:mkui:$macrofocusVersion")
        implementation("com.treemap:treemap:$macrofocusVersion")
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
                exclude("build/**")
            }

//            from("TreeMap API for Kotlin-Swing Developer Guide.pdf")
            from("../treemap/build/dokka/html/") {
                into("dokka")
            }
        }
    }
}