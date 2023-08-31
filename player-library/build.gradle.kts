plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.Google.material)
    testImplementation(Dependencies.Testing.junit)
    androidTestImplementation(Dependencies.Testing.androidXjunit)
    androidTestImplementation(Dependencies.Testing.espressoCore)

    // player library
    implementation(Dependencies.Google.exoplayer)

    implementation(Dependencies.Coroutines.coroutinesCore)
    implementation(Dependencies.AndroidX.liveData)
}
