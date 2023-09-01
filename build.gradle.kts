import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id(Plugins.Android.application) version Plugins.Android.version apply false
    id(Plugins.Android.library) version Plugins.Android.version apply false
    id(Plugins.Detekt.plugin) version Plugins.Detekt.version
    kotlin(Plugins.Kotlin.jvm) version Plugins.Kotlin.version
}

dependencies {
    implementation(kotlin(Plugins.Kotlin.standardLibrary))
    detektPlugins(Plugins.Detekt.formatterDependency)
}
repositories {
    google()
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

tasks.register("detektAll", Detekt::class.java) {
    val kotlinFiles = "**/*.kt"
    val resourceFiles = "**/res/**"
    val buildFiles = "**/build/**"

    autoCorrect = true
    parallel = true
    buildUponDefaultConfig = true
    allRules = false
    setSource(file(projectDir))
    config.setFrom("$rootDir/staticAnalysis/detektConfig.yml")
    include(kotlinFiles)
    exclude(buildFiles, resourceFiles)
}