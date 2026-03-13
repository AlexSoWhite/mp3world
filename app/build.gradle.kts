plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.detekt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.nafanya.mp3world"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas")
            }
        }
    }
    flavorDimensions += "version"
    productFlavors {
        create("demo") {
            dimension = "version"
            applicationIdSuffix = ".demo"
            versionNameSuffix = "-demo"
        }
        create("full") {
            dimension = "version"
            applicationIdSuffix = ".full"
            versionNameSuffix = "-full"
        }
    }
    sourceSets {
        // Adds exported schema location as test app assets.
        getByName("androidTest").assets.directories.add("$projectDir/schemas")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            val proguards = fileTree("proguard") {
                include("*.pro")
            }
            @Suppress("SpreadOperator")
            proguardFiles(*proguards.toList().toTypedArray())
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    namespace = "com.nafanya.mp3world"
}

dependencies {

    implementation(project(":player-library"))

    implementation(libs.androidx.appCompat)
    // to use 'by viewModels()'
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.androidx.media3Ui)
    implementation(libs.androidx.media3Session)

    implementation(libs.google.material)

    implementation(libs.squareup.okhttp3)
    implementation(libs.jsoup)

    implementation(libs.bumptech.glide)
    kapt(libs.bumptech.glideCompiler)

    implementation(libs.androidx.roomKtx)
    implementation(libs.google.gson)
    kapt(libs.androidx.roomCompiler)
    androidTestImplementation(libs.androidx.roomTesting)

    implementation(libs.google.dagger)
    kapt(libs.google.daggerCompiler)

    // debugImplementation because LeakCanary should only run in debug builds.
    // debugImplementation(Dependencies.Debug.leakCanary)

    testImplementation(libs.junit)
    testImplementation(libs.android.coreTesting)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espressoCore)
}
