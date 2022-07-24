package com.nafanya.mp3world.core.entrypoint.di

import com.nafanya.mp3world.core.entrypoint.MainActivity
import dagger.Subcomponent

@Subcomponent(modules = [EntrypointModule::class])
interface EntrypointComponent {

    fun inject(activity: MainActivity)
}
