package com.nafanya.mp3world.model.dependencies

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

/**
 * Class that provides access to the application context.
 */
@HiltAndroidApp
class PlayerApplication : Application() {

//    companion object {
//        lateinit var application: Application
//
//        fun context(): Context {
//            return application.applicationContext
//        }
//    }
}
