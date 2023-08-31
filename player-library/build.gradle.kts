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

detekt {
    autoCorrect = true
    buildUponDefaultConfig = true
    allRules = false
    config = files("$rootDir/staticAnalysis/detektConfig.yml")
}

dependencies {

    implementation(Dependencies.AndroidX.liveData)
    implementation(Dependencies.Coroutines.coroutinesCore)
    implementation(Dependencies.Google.exoplayer)

    testImplementation(Dependencies.Testing.junit)
    androidTestImplementation(Dependencies.Testing.androidXjunit)
    androidTestImplementation(Dependencies.Testing.espressoCore)
}
