plugins {
    id(Plugins.Android.application)
    id(Plugins.Detekt.plugin)
    kotlin(Plugins.Kotlin.android)
    kotlin(Plugins.Kotlin.kapt)
}

android {
    compileSdk = BuildConfig.compileSdk

    defaultConfig {
        applicationId = "com.nafanya.mp3world"
        minSdk = BuildConfig.minSdk
        targetSdk = BuildConfig.targetSdk
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
            @Suppress("SpreadOperator")
            proguardFiles(*proguards.toList().toTypedArray())
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(project(Projects.playerLibrary))

    implementation(Dependencies.AndroidX.appCompat)
    implementation(Dependencies.AndroidX.coreKtx)
    implementation(Dependencies.AndroidX.constraintLayout)
    implementation(Dependencies.AndroidX.viewModel)
    implementation(Dependencies.AndroidX.paging)
    // to use 'by viewModels()'
    implementation(Dependencies.AndroidX.fragmentKtx)
    implementation(Dependencies.AndroidX.swipeRefreshLayout)

    implementation(Dependencies.Google.material)
    implementation(Dependencies.Google.exoplayer)

    implementation(Dependencies.Coroutines.coroutinesCore)
    implementation(Dependencies.Coroutines.coroutinesAndroid)

    implementation(Dependencies.Web.okHttp3)
    implementation(Dependencies.Web.jsoup)

    implementation(Dependencies.Images.glide)
    kapt(Dependencies.Images.glideCompiler)

    implementation(Dependencies.LocalDb.roomKtx)
    implementation(Dependencies.LocalDb.gson)
    kapt(Dependencies.LocalDb.roomCompiler)
    androidTestImplementation(Dependencies.LocalDb.roomTesting)

    implementation(Dependencies.DI.dagger)
    kapt(Dependencies.DI.daggerCompiler)

    // debugImplementation because LeakCanary should only run in debug builds.
    // debugImplementation(Dependencies.Debug.leakCanary)

    testImplementation(Dependencies.Testing.junit)
    testImplementation(Dependencies.Testing.coreTesting)
    androidTestImplementation(Dependencies.Testing.androidXjunit)
    androidTestImplementation(Dependencies.Testing.espressoCore)
}

kapt {
    correctErrorTypes = true
}
