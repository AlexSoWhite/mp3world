object Dependencies {

    object AndroidX {
        
        object Versions {
            const val appCompat = "1.4.1"
            const val coreCtx = "1.7.0"
            const val constraintLayot = "2.1.3"
            const val viewModelKtx = "2.4.1"
            const val lifecycleService = "2.5.1"
            const val paging = "3.1.1"
            const val activityKtx = "1.4.0"
            const val fragmentKtx = "1.1.0"
            const val swipeRefreshLayout = "1.1.0"
            const val liveData = "2.5.1"
        }
        
        const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreCtx}"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraintLayot}"
        const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewModelKtx}"
        // TODO obsolete
        @Deprecated("mb obsolete")
        const val lidecycleService = "androidx.lifecycle:lifecycle-service:${Versions.lifecycleService}"
        const val paging = "androidx.paging:paging-runtime-ktx:${Versions.paging}"
        const val activityKtx = "androidx.activity:activity-ktx:${Versions.activityKtx}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
        const val swipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"
        const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.liveData}"
    }

    object Coroutines {

        object Versions {
            const val coroutines = "1.6.0"
        }

        const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"
        const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
        // TODO obsolete?
        @Deprecated("mb obsolete")
        const val playServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:${Versions.coroutines}"
    }

    object Google {

        object Versions {
            const val material = "1.5.0"
            const val exoplayer = "2.17.1"
        }

        const val material = "com.google.android.material:material:${Versions.material}"
        const val exoplayer = "com.google.android.exoplayer:exoplayer:${Versions.exoplayer}"
    }

    object DI {

        object Versions {
            const val dagger = "2.41"
        }

        const val dagger = "com.google.dagger:dagger:${Versions.dagger}"
        const val daggerCOmpiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }

    object Images {

        object Versions {
            const val glide = "4.14.2"
        }

        const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
        const val glideCompiler = "com.github.bumptech.glide:compiler:${Versions.glide}"
    }

    object Web {

        object Versions {
            const val okHttp3 = "4.9.2"
            const val jsoup = "1.13.1"
        }

        const val okHttp3 = "com.squareup.okhttp3:okhttp:${Versions.okHttp3}"
        const val jsoup = "org.jsoup:jsoup:${Versions.jsoup}"
    }

    object LocalDb {

        object Versions {
            const val room = "2.4.2"
            const val gson = "2.9.0"
        }

        const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
        const val roomTesting = "androidx.room:room-testing:${Versions.room}"
        const val gson = "com.google.code.gson:gson:${Versions.gson}"
    }

    object Testing {

        object Versions {
            const val junit = "4.13.2"
            const val coreTesting = "1.1.1"
            const val androidXjunit = "1.1.3"
            const val espressoCore = "3.4.0"
        }

        const val junit = "junit:junit:${Versions.junit}"
        const val coreTesting = "android.arch.core:core-testing:${Versions.coreTesting}"
        const val androidXjunit = "androidx.test.ext:junit:${Versions.androidXjunit}"
        const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espressoCore}"
    }

    object Debug {

        object Versions {
            const val leakCanary = "2.9.1"
        }

        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    }
}
