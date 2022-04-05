package com.nafanya.mp3world.model.dependencies

import android.app.Application
import android.content.Context

/**
 * Class that provides access to the application context.
 */
object PlayerApplication {

    lateinit var application: Application

    fun context(): Context {
        return application.applicationContext
    }
}
