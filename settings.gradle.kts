rootProject.name = "ledgers"
include("components")
include("core")
include("application")

pluginManagement {
    plugins {
        kotlin("jvm") version "1.9.25"
        kotlin("multiplatform") version "1.9.25"
        kotlin("plugin.serialization") version "1.9.25"
    }
}
