package com.nafanya.mp3world.model.dependencies

import android.app.Application
import android.content.Context

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
