package com.nafanya.mp3world.model.dependencies

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Class that provides access to the application context.
 */
@HiltAndroidApp
class PlayerApplication : Application()
