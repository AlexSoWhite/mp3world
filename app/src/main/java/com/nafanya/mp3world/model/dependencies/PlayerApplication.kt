package com.nafanya.mp3world.model.dependencies

import android.app.Application
import android.content.Context

/**
 * Class that provides access to the application context.
 */
class PlayerApplication : Application() {

    companion object {

        private var application: Application? = null

        fun context(): Context {
            return application!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}
