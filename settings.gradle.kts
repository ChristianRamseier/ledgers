rootProject.name = "ledgers"
include("core")
include("components")
include("application")

pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.22"
        kotlin("multiplatform") version "1.9.22"
        kotlin("plugin.serialization") version "1.9.22"
    }
}
