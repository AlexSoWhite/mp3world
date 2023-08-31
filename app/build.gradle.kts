plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = 32

    defaultConfig {
        applicationId = "com.nafanya.mp3world"
        minSdk = 21
        targetSdk = 32
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf("room.schemaLocation" to "$projectDir/schemas".toString())
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
        getByName("androidTest").assets.srcDirs("$projectDir/schemas")
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            val proguards = fileTree("proguard") {
                include("*.pro")
            }
            proguardFiles(*proguards.toList().toTypedArray())
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget =("1.8")
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(":player-library"))

    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    implementation("androidx.lifecycle:lifecycle-service:2.5.1")

    // player library
    implementation("com.google.android.exoplayer:exoplayer:2.17.1")

    // coroutines and viewModel
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.4.1")

    // okhttp3 to make requests to internet
    implementation("com.squareup.okhttp3:okhttp:4.9.2")

    // jsoup to parse html pages
    implementation("org.jsoup:jsoup:1.13.1")

    // glide to load images
    implementation("com.github.bumptech.glide:glide:4.11.0")

    // local storage, SQLite wrapper Room
    val roomVersion = "2.4.2"
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    // optional - Test helpers
    androidTestImplementation("androidx.room:room-testing:$roomVersion")
    // gson to help room serialize lists
    implementation("com.google.code.gson:gson:2.9.0")

    // paging
    val pagingVersion = "3.1.1"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")

    implementation("androidx.activity:activity-ktx:1.4.0")

    implementation("androidx.fragment:fragment-ktx:1.1.0")

    // dagger dependency injection
    implementation("com.google.dagger:dagger:2.41")
    kapt("com.google.dagger:dagger-compiler:2.41")

    // debugImplementation because LeakCanary should only run in debug builds.
    // debugImplementation("com.squareup.leakcanary:leakcanary-android:2.9.1")

    // swipe to refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // glide for images
    implementation("com.github.bumptech.glide:glide:4.14.2")
    kapt("com.github.bumptech.glide:compiler:4.14.2")

    testImplementation("junit:junit:4.13.2")
    testImplementation("android.arch.core:core-testing:1.1.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}

kapt {
    correctErrorTypes = true
}
