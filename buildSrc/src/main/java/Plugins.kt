object Plugins {

    object Detekt {
        const val version = "1.18.1"
        const val plugin = "io.gitlab.arturbosch.detekt"
        const val formatterDependency = "io.gitlab.arturbosch.detekt:detekt-formatting:$version"
    }

    object Android {
        const val application = "com.android.application"
        const val library = "com.android.library"
        const val version = "7.1.3"
    }

    object Kotlin {
        const val android = "android"
        const val kapt = "kapt"
        const val jvm = "jvm"
        const val standardLibrary = "stdlib-jdk8"
        const val version = "1.6.21"
    }
}
