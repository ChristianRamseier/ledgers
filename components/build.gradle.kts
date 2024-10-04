import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.github.node-gradle.node") version "3.1.0"
}

repositories {
    mavenCentral()
}

node {
    version.set("20.17.0")
    npmVersion.set("10.8.2")
    distBaseUrl.set("https://nodejs.org/dist")
    npmInstallCommand.set("install")
    download.set(true)
    workDir.set(File("${projectDir}/webapp/build/nodejs"))
    npmWorkDir.set(File("${projectDir}/webapp/build/npm"))
    nodeProjectDir.set(File("${projectDir}/webapp"))
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<com.github.gradle.node.npm.task.NpxTask>("buildAngularApp") {
    dependsOn(tasks.getByName("npmInstall"))
    workingDir.set(file("webapp"))
    command.set("ng")
    args.set(listOf("build", "--output-hashing", "none"))
    inputs.files("package.json", "package-lock.json", "angular.json", "tsconfig.json", "tsconfig.app.json")
    inputs.dir("webapp/src")
    inputs.dir(fileTree("webapp/node_modules").exclude(".cache"))
    outputs.dir("webapp/dist")
}


tasks {
    withType<Copy> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

