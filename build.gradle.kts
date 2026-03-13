import io.gitlab.arturbosch.detekt.Detekt
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

@Suppress("DSL_SCOPE_VIOLATION")
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.detekt)
}

dependencies {
    detektPlugins(libs.detekt.formatting)
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