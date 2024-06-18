plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

kotlin {


    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_21.toString()
        }
        tasks.withType<Test> {
            useJUnitPlatform()
        }
    }

    js {
        browser {
        }
        binaries.library()
        generateTypeScriptDefinitions()
    }

    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.js.ExperimentalJsExport")
            }
        }
        commonMain {
            dependencies {
                implementation("com.benasher44:uuid:0.8.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }

        }
        jvmTest {
            dependencies {
                implementation("org.assertj:assertj-core:3.24.2")
                implementation("org.junit.jupiter:junit-jupiter:5.10.1")
            }
        }
    }

}





