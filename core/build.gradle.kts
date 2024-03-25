import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinJsCompilation

plugins {
    kotlin("multiplatform")
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
        commonMain {
            dependencies {
                implementation("com.benasher44:uuid:0.8.4")
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





