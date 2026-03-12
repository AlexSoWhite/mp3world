plugins {
    id(Plugins.Android.library)
    id(Plugins.Detekt.plugin)
    kotlin(Plugins.Kotlin.android)
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk

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
    implementation(Dependencies.Coroutines.coroutinesCore)
    implementation(Dependencies.AndroidX.media3Exoplayer)
    implementation(Dependencies.AndroidX.media3Session)
    implementation(Dependencies.AndroidX.media3ExoplayerDash)
    implementation(Dependencies.AndroidX.media3Ui)

    testImplementation(Dependencies.Testing.junit)
    androidTestImplementation(Dependencies.Testing.androidXjunit)
    androidTestImplementation(Dependencies.Testing.espressoCore)
}
