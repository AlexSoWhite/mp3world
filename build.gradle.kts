import io.gitlab.arturbosch.detekt.Detekt

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.compose.compiler) apply false
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

kotlin {
    compilerOptions {
        languageVersion = org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_0
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8
    }
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